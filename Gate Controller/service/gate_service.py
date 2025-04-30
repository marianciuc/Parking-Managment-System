import configparser
import logging
import os
from enum import Enum
from typing import Optional

from enums.gate_status import GateStatus
from enums.gate_type import GateType
from service.gate_IoT_service import GateIoTService, Position


class OperationMode(Enum):
    MANUAL = "manual"
    AUTOMATIC = "automatic"


class GateService:

    CONFIG_PATH = '../config.ini'

    def __init__(self, gate_iot_service: GateIoTService):
        self.logger = logging.getLogger(__name__)
        self.gate_iot_service = gate_iot_service
        self.mode = OperationMode.AUTOMATIC
        self.status = GateStatus.CLOSE

        try:
            self._load_config()
        except Exception as e:
            self.logger.error(f"Configuration loading error: {str(e)}")
            raise ConfigurationError("Failed to load gate configuration") from e

        self._sync_status()

    def _load_config(self) -> None:
        config_path = os.path.join(os.path.dirname(__file__), self.CONFIG_PATH)

        if not os.path.exists(config_path):
            self.logger.error(f"Configuration file not found: {config_path}")
            raise FileNotFoundError(f"Configuration file not found: {config_path}")

        config = configparser.ConfigParser()
        config.read(config_path)

        try:
            self.type = GateType(config.get('app', 'type'))
            self.name = config.get('app', 'name')
            self.api_key = config.get('app', 'api_key')
            self.port = config.get('app', 'port')
            self.host = config.get('app', 'host')
            self.id = config.get('app', 'id', fallback=None)
        except (configparser.NoSectionError, configparser.NoOptionError) as e:
            self.logger.error(f"Required configuration parameter is missing: {str(e)}")
            raise
        except ValueError as e:
            self.logger.error(f"Invalid parameter value: {str(e)}")
            raise

        self.logger.info(f"Configuration loaded: gate {self.name} of type {self.type}")

    def _sync_status(self) -> None:
        try:
            self._update_status()
            self.logger.info(f"Gate status synchronized: {self.status}")
        except Exception as e:
            self.logger.error(f"Error synchronizing gate state: {str(e)}")
            self.status = GateStatus.CLOSE

    def _update_status(self) -> None:
        current_position = self.gate_iot_service.get_position()

        if current_position == Position.CLOSE:
            self.status = GateStatus.CLOSE
        else:
            self.status = GateStatus.OPEN

        self.logger.debug(f"Gate state updated: {self.status}")

    def open(self) -> bool:
        if self.mode == OperationMode.MANUAL:
            self.logger.info("Opening gate in manual mode")
            return self._perform_open()
        else:
            self.logger.info("Opening gate in automatic mode")
            return self.automatic_open()

    def _perform_open(self) -> bool:
        try:
            self.logger.debug("Sending open command to gate")
            self.gate_iot_service.open()
            self._update_status()
            return self.status == GateStatus.OPEN
        except Exception as e:
            self.logger.error(f"Error opening gate: {str(e)}")
            return False

    def close(self) -> bool:
        if self.mode == OperationMode.MANUAL:
            self.logger.info("Closing gate in manual mode")
            return self._perform_close()
        else:
            self.logger.info("Closing gate in automatic mode")
            return self.automatic_close()

    def _perform_close(self) -> bool:
        try:
            self.logger.debug("Sending close command to gate")
            self.gate_iot_service.close()
            self._update_status()
            return self.status == GateStatus.CLOSE
        except Exception as e:
            self.logger.error(f"Error closing gate: {str(e)}")
            return False

    def manually_open(self) -> bool:
        self.set_mode(OperationMode.MANUAL)
        return self._perform_open()

    def manually_close(self) -> bool:
        self.set_mode(OperationMode.MANUAL)
        return self._perform_close()

    def automatic_open(self) -> bool:
        if self.mode != OperationMode.AUTOMATIC:
            self.logger.warning("Attempt to automatically open in manual mode")
            return False

        return self._perform_open()

    def automatic_close(self) -> bool:
        if self.mode != OperationMode.AUTOMATIC:
            self.logger.warning("Attempt to automatically close in manual mode")
            return False

        return self._perform_close()

    def set_mode(self, mode: OperationMode) -> None:
        if self.mode != mode:
            old_mode = self.mode
            self.mode = mode
            self.logger.info(f"Gate mode changed: {old_mode} -> {mode}")

            if mode == OperationMode.AUTOMATIC and self.status == GateStatus.OPEN:
                self.logger.info("Automatically closing gate when switching to automatic mode")
                self._perform_close()

    def get_status(self) -> GateStatus:
        self._update_status()
        return self.status

    def is_manual_mode(self) -> bool:
        return self.mode == OperationMode.MANUAL


class ConfigurationError(Exception):
    pass
