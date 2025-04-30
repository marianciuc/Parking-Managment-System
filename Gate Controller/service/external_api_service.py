import configparser
import logging
import os
from functools import lru_cache
from typing import Dict, Any, Optional, Union, Tuple
from urllib.parse import urljoin

import requests
from requests.exceptions import RequestException, ConnectionError, Timeout
from service.gate_service import GateService


class ApiError(Exception):

    def __init__(self, status_code: int, message: str):
        self.status_code = status_code
        self.message = message
        super().__init__(f"API Error {status_code}: {message}")


class ExternalApiService:
    API_ENDPOINTS = {
        "REGISTER": "/api/v1/gates/register",
        "UNREGISTER": "/api/v1/gates/unregister",
        "PREPARE": "/prepare",
        "PREPARE_END": "/prepare-end",
        "START": "/start",
        "END": "/end",
        "CANCEL": "/cancel",
        "CANCEL_PREPARED_END": "/cancel-prepared-end"
    }

    REQUEST_TIMEOUT = 10

    def __init__(self, gate: GateService, config_path: str = 'config.ini'):
        self.gate = gate
        self.base_url = self._load_config(config_path)
        self.logger = logging.getLogger(__name__)

        self._session = requests.Session()

    @staticmethod
    @lru_cache(maxsize=1)
    def _load_config(config_path: str) -> str:
        config = configparser.ConfigParser()
        config_file = os.path.abspath(config_path)

        if not os.path.exists(config_file):
            raise FileNotFoundError(f"Configuration file not found: {config_file}")

        config.read(config_file)

        try:
            return config.get('app', 'backend_uri')
        except (configparser.NoSectionError, configparser.NoOptionError) as e:
            raise KeyError(f"Configuration error: {e}")

    def _get_headers(self) -> Dict[str, str]:
        return {
            "X-Api-Key": self.gate.api_key,
            "Content-Type": "application/json",
            "Accept": "application/json"
        }

    def _make_request(
            self,
            method: str,
            endpoint: str,
            params: Optional[Dict[str, Any]] = None,
            json_data: Optional[Dict[str, Any]] = None
    ) -> Dict[str, Any]:
        url = urljoin(self.base_url, endpoint)
        headers = self._get_headers()

        self.logger.debug(f"Отправка {method} запроса к {url}")

        try:
            response = self._session.request(
                method=method,
                url=url,
                headers=headers,
                params=params,
                json=json_data,
                timeout=self.REQUEST_TIMEOUT
            )

            response_data = {
                "status_code": response.status_code,
                "text": response.text.strip() if response.text else "",
                "success": 200 <= response.status_code < 300
            }

            if not response_data["success"]:
                self.logger.warning(
                    f"API вернул ошибку: {response.status_code} - {response.text}"
                )

            return response_data

        except ConnectionError as e:
            self.logger.error(f"Ошибка соединения при запросе к {endpoint}: {e}")
            return self._format_error_response(str(e))

        except Timeout as e:
            self.logger.error(f"Тайм-аут при запросе к {endpoint}: {e}")
            return self._format_error_response(f"Превышено время ожидания: {e}")

        except RequestException as e:
            self.logger.error(f"Ошибка при отправке запроса к {endpoint}: {e}")
            return self._format_error_response(str(e))

    @staticmethod
    def _format_error_response(error_message: str) -> Dict[str, Any]:
        return {
            "status_code": 0,
            "text": error_message,
            "success": False,
            "error": error_message
        }

    def _get_session_id(self, plate_number: str) -> Tuple[bool, str]:
        if not plate_number:
            return False, "Invalid plate number"

        params = {"plate": plate_number}
        response = self._make_request("GET", "/api/v1/sessions/find-by-plate", params=params)

        if response["success"]:
            session_id = response["text"].strip('"')
            return True, session_id

        return False, response.get("text", "Unknown error")

    def register_gate(self) -> bool:
        # Преобразуем GateType в строковое представление для JSON сериализации
        gate_type = str(self.gate.type)  # или self.gate.type.value или self.gate.type.name если это Enum
        gate_status = str(self.gate.status)
        gate_host = str(self.gate.host)
        gate_port = str(self.gate.port)

        data = {
            "type": gate_type,  # Используем строковое представление типа ворот
            "status": gate_status,
            "host": gate_host,
            "port": gate_port,
            "isManualMode": str(self.gate.is_manual_mode()).lower(),
        }

        response = self._make_request("POST", self.API_ENDPOINTS["REGISTER"], json_data=data)

        if response["success"]:
            self.gate.id = response["text"].strip('"')
            self.logger.info(f"Ворота успешно зарегистрированы! ID: {self.gate.id}")
            return True

        self.logger.error(
            f"Ошибка при регистрации ворот. Код: {response['status_code']}, {response['text']}"
        )
        return False

    def unregister_gate(self) -> bool:
        if not self.gate.id:
            self.logger.error("Невозможно отменить регистрацию: отсутствует ID ворот")
            return False

        params = {"gateId": self.gate.id}
        response = self._make_request("POST", self.API_ENDPOINTS["UNREGISTER"], params=params)

        if response["success"]:
            self.logger.info("Ворота успешно удалены из системы")
            self.gate.id = None
            return True

        self.logger.error(
            f"Ошибка при отмене регистрации ворот: {response['status_code']}, {response['text']}"
        )
        return False

    def prepare_session(self, plate_number: str) -> Dict[str, Any]:
        if not plate_number:
            return {"error": "Не указан номер машины"}

        params = {"plate": plate_number}
        response = self._make_request("POST", self.API_ENDPOINTS["PREPARE"], params=params)

        if response["success"]:
            session_id = response["text"].strip('"')
            self.logger.info(f"Въезд подготовлен, номер: {plate_number}, SessionID: {session_id}")
            return {"plate": plate_number, "sessionId": session_id}

        self.logger.error(
            f"Ошибка при подготовке въезда. Код: {response['status_code']}, {response['text']}"
        )
        return {"error": f"HTTP {response['status_code']}", "details": response["text"]}

    def start_session(self, plate_number: str) -> Dict[str, Any]:
        success, session_or_error = self._get_session_id(plate_number)
        if not success:
            return {"error": f"Не удалось получить идентификатор сессии: {session_or_error}"}

        response = self._make_request("POST", f"/{session_or_error}{self.API_ENDPOINTS['START']}")

        if response["success"]:
            self.logger.info(f"Сессия {session_or_error} запущена")
            return {"message": "Сессия успешно запущена", "sessionId": session_or_error}

        self.logger.error(
            f"Ошибка при запуске сессии {session_or_error}. Код: {response['status_code']}, {response['text']}"
        )
        return {"error": f"HTTP {response['status_code']}", "details": response["text"]}

    def stop_session(self, plate_number: str) -> Dict[str, Any]:
        if not plate_number:
            return {"error": "Не указан номер машины"}

        params = {"plate": plate_number}
        response = self._make_request("POST", self.API_ENDPOINTS["END"], params=params)

        if response["success"]:
            self.logger.info(f"Сессия для {plate_number} завершена")
            return {"message": "Сессия успешно завершена"}

        self.logger.error(
            f"Ошибка при завершении сессии для {plate_number}. Код: {response['status_code']}, {response['text']}"
        )
        return {"error": f"HTTP {response['status_code']}", "details": response["text"]}

    def cancel_session(self, plate_number: str) -> Dict[str, Any]:
        success, session_or_error = self._get_session_id(plate_number)
        if not success:
            return {"error": f"Не удалось получить идентификатор сессии: {session_or_error}"}

        response = self._make_request("POST", f"/{session_or_error}{self.API_ENDPOINTS['CANCEL']}")

        if response["success"]:
            self.logger.info(f"Сессия {session_or_error} отменена")
            return {"message": "Сессия успешно отменена", "sessionId": session_or_error}

        self.logger.error(
            f"Ошибка при отмене сессии {session_or_error}. Код: {response['status_code']}, {response['text']}"
        )
        return {"error": f"HTTP {response['status_code']}", "details": response["text"]}

    def prepare_end(self, plate_number: str) -> Dict[str, Any]:
        success, session_or_error = self._get_session_id(plate_number)
        if not success:
            return {"error": f"Не удалось получить идентификатор сессии: {session_or_error}"}

        response = self._make_request(
            "POST",
            f"/{session_or_error}{self.API_ENDPOINTS['PREPARE_END']}"
        )

        if response["success"]:
            self.logger.info(f"Подготовка к завершению сессии {session_or_error} успешно выполнена")
            return {"message": "Подготовка к завершению сессии выполнена", "sessionId": session_or_error}

        self.logger.error(
            f"Ошибка при подготовке к завершению сессии {session_or_error}. "
            f"Код: {response['status_code']}, {response['text']}"
        )
        return {"error": f"HTTP {response['status_code']}", "details": response["text"]}

    def cancel_prepare_end(self, plate_number: str) -> Dict[str, Any]:
        if not plate_number:
            return {"error": "Не указан номер машины"}

        params = {"plate": plate_number}
        response = self._make_request(
            "POST",
            self.API_ENDPOINTS["CANCEL_PREPARED_END"],
            params=params
        )

        if response["success"] or response["status_code"] == 204:
            self.logger.info(f"Подготовка к завершению сессии {plate_number} отменена")
            return {"message": "Отмена подготовки к завершению сессии выполнена успешно"}

        self.logger.error(
            f"Ошибка при отмене подготовки к завершению сессии {plate_number}. "
            f"Код: {response['status_code']}, {response['text']}"
        )
        return {"error": f"HTTP {response['status_code']}", "details": response["text"]}

    def __del__(self):
        if hasattr(self, '_session'):
            self._session.close()
