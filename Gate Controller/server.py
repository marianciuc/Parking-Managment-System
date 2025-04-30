from fastapi import FastAPI, Query, HTTPException
import logging
import configparser
import uvicorn
import os
import threading
import time

from service.led_service import LedService
from service.camera_service import CameraService
from service.gate_service import GateService, OperationMode
from service.gate_IoT_service import GateIoTService
# from service.recognition_service import RecognitionService
from service.sensors_handler import SensorsHandler
from service.entry_exit_service import EntryExitService
from service.external_api_service import ExternalApiService

# Конфигурация
config = configparser.ConfigParser()
config.read(os.path.join(os.path.dirname(__file__), 'config.ini'))

# Логирование
logging.basicConfig(level=logging.DEBUG, format="%(asctime)s - %(levelname)s - %(message)s")

# Создаем экземпляры
gate_iot_service = GateIoTService()
gate = GateService(gate_iot_service)
external_api_service = ExternalApiService(gate)
app = FastAPI()

# Переменные для настройки сервера
server_config = {
    "log_level": logging.INFO,
    "port": gate.port,
    "host": "0.0.0.0"
}


# Функция для изменения уровня логирования
def set_log_level(level_str):
    levels = {
        "debug": logging.DEBUG,
        "info": logging.INFO,
        "warning": logging.WARNING,
        "error": logging.ERROR,
        "critical": logging.CRITICAL
    }
    if level_str.lower() in levels:
        new_level = levels[level_str.lower()]
        logging.getLogger().setLevel(new_level)
        server_config["log_level"] = new_level
        print(f"Уровень логирования изменен на {level_str.upper()}")
    else:
        print(f"Неверный уровень логирования. Доступные уровни: {', '.join(levels.keys())}")


# Функция для консольного меню
def console_menu():
    time.sleep(2)  # Ждем немного, чтобы сервер успел запуститься

    while True:
        print("\n===== МЕНЮ УПРАВЛЕНИЯ СЕРВЕРОМ =====")
        print("1. Открыть ворота")
        print("2. Закрыть ворота")
        print("3. Переключить режим работы ворот")
        print("4. Показать статус ворот")
        print("5. Изменить уровень логирования")
        print("6. Показать текущие настройки")
        print("0. Выход")

        choice = input("\nВыберите пункт меню: ")

        if choice == "1":
            try:
                gate.open()
                gate.set_mode(OperationMode.MANUAL)
                print("Ворота открыты успешно")
            except Exception as e:
                print(f"Ошибка при открытии ворот: {e}")

        elif choice == "2":
            try:
                gate.close()
                gate.set_mode(OperationMode.MANUAL)
                print("Ворота закрыты успешно")
            except Exception as e:
                print(f"Ошибка при закрытии ворот: {e}")

        elif choice == "3":
            try:
                current_mode = gate.is_manual_mode()
                gate.set_mode(OperationMode.AUTOMATIC if current_mode else OperationMode.MANUAL)
                new_mode = "Ручной" if not current_mode else "Автоматический"
                print(f"Режим работы ворот изменен на: {new_mode}")
            except Exception as e:
                print(f"Ошибка при изменении режима: {e}")

        elif choice == "4":
            status = gate.get_status()
            mode = "Ручной" if gate.is_manual_mode() else "Автоматический"
            print(f"Статус ворот: {status}")
            print(f"Режим работы: {mode}")

        elif choice == "5":
            level = input("Введите уровень логирования (debug/info/warning/error/critical): ")
            set_log_level(level)

        elif choice == "6":
            print("\n----- Текущие настройки -----")
            print(f"Хост: {server_config['host']}")
            print(f"Порт: {server_config['port']}")
            log_levels = {
                logging.DEBUG: "DEBUG",
                logging.INFO: "INFO",
                logging.WARNING: "WARNING",
                logging.ERROR: "ERROR",
                logging.CRITICAL: "CRITICAL"
            }
            print(f"Уровень логирования: {log_levels.get(server_config['log_level'], 'Неизвестный')}")
            print("---------------------------")

        elif choice == "0":
            print("Выход из меню. Сервер продолжает работу.")
            break

        else:
            print("Неверный выбор. Пожалуйста, выберите пункт из меню.")


@app.post("/api/v1/gate/open")
async def open_gate():
    try:
        gate.open()
        gate.set_mode(OperationMode.MANUAL)
        return {"message": "Gate opened successfully"}
    except Exception as e:
        logging.error(f"Ошибка при открытии ворот: {e}")
        raise HTTPException(status_code=500, detail="Ошибка при открытии ворот")


@app.post("/api/v1/gate/close")
async def close_gate():
    try:
        gate.close()
        gate.set_mode(OperationMode.MANUAL)
        return {"message": "Gate closed successfully"}
    except Exception as e:
        logging.error(f"Ошибка при закрытии ворот: {e}")
        raise HTTPException(status_code=500, detail="Ошибка при закрытии ворот")


@app.post("/api/v1/gate")
async def set_gate_mode(mode: bool = Query(...)):
    try:
        gate.set_mode(OperationMode.MANUAL if mode else OperationMode.AUTOMATIC)
        return {"message": f"Gate mode set to {'Manual' if mode else 'Auto'}"}
    except Exception as e:
        logging.error(f"Ошибка при изменении режима: {e}")
        raise HTTPException(status_code=500, detail="Ошибка при изменении режима ворот")


@app.get("/api/v1/gate/status")
async def get_gate_status():
    return {
        "status": gate.get_status(),
        "isManualMode": gate.is_manual_mode()
    }


if __name__ == "__main__":
    external_api_service.register_gate()
    camera_service = CameraService()
    led_service = LedService()
    # recognition_service = RecognitionService()
    entry_exit_service = EntryExitService(external_api_service=external_api_service, gate_service=gate,
                                          camera_service=camera_service, led_service=led_service)
                                          # recognition_service=recognition_service)


    print("Сервер запущен. Для доступа к меню используйте консоль.")

    try:
        uvicorn.run(app, host=server_config["host"], port=server_config["port"])
        sensor_handler = SensorsHandler(entry_exit_service=entry_exit_service)
        menu_thread = threading.Thread(target=console_menu, daemon=True)
        menu_thread.start()
        target = sensor_handler.start_main_handler()
    finally:
        external_api_service.unregister_gate()
