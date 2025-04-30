import RPi.GPIO as GPIO
import time

from enums.sensor_type import SensorType
from service.entry_exit_service import EntryExitService


class SensorsHandler:

    def __init__(self, entry_exit_service: EntryExitService, distance: int = 30):
        self.entry_exit_service = entry_exit_service

        self.distance: int = distance
        self.has_active_object = False
        self.ECHO_MAIN = 24  # Пин для ультразвукового датчика (Echo)
        self.TRIG_MAIN = 23  # Пин для ультразвукового датчика (Trig)

        GPIO.setup(self.ECHO_MAIN, GPIO.IN)
        GPIO.setup(self.TRIG_MAIN, GPIO.OUT)

    def _distance_main(self):
        GPIO.output(self.TRIG_MAIN, True)
        time.sleep(0.00001)
        GPIO.output(self.TRIG_MAIN, False)

        start = time.time()
        end = time.time()
        while GPIO.input(self.ECHO_MAIN) == 0:
            start = time.time()

        while GPIO.input(self.ECHO_MAIN) == 1:
            end = time.time()

        duration = end - start
        dist = (duration * 34300) / 2
        return dist

    # В sensors_handler.py
    def start_main_handler(self):
        try:
            distance_history = []
            history_size = 5

            while True:
                try:
                    dist = self._distance_main()
                    print(f"Измеренное расстояние: {dist:.2f} см")

                    distance_history.append(dist)
                    if len(distance_history) > history_size:
                        distance_history.pop(0)

                    if len(distance_history) == history_size:
                        avg_dist = sum(distance_history) / len(distance_history)

                        if avg_dist <= self.distance and not self.has_active_object:
                            print("Обнаружен объект (стабильное измерение)")
                            self.has_active_object = True
                            if not self.entry_exit_service.is_processing:
                                self.entry_exit_service.process(SensorType.FRONT)
                            else:
                                print("Обработка уже выполняется. Пропускаем запуск.")
                        elif avg_dist > self.distance and self.has_active_object:
                            print("Объект покинул зону (стабильное измерение)")
                            self.has_active_object = False
                            self.entry_exit_service.object_left()

                    time.sleep(0.1)
                except Exception as e:
                    print(f"Ошибка при измерении или обработке: {e}")
                    time.sleep(1)

        except KeyboardInterrupt:
            print("Остановка обработчика датчиков")

    def start_back_handler(self):
        print("This method is empty")
