import logging
from typing import Optional

from enums.gate_type import GateType
from enums.sensor_type import SensorType
from service.camera_service import CameraService
from service.external_api_service import ExternalApiService
from service.gate_service import GateService
from service.led_service import LedService
from service.recognition_service import RecognitionService


class EntryExitService:

    MAX_RECOGNITION_ATTEMPTS = 10

    def __init__(
            self,
            external_api_service: ExternalApiService,
            gate_service: GateService,
            camera_service: CameraService,
            led_service: LedService,
            # recognition_service: RecognitionService,
    ):
        self.external_api_service = external_api_service
        self.led_service = led_service
        # self.recognition_service = recognition_service
        self.camera_service = camera_service
        self.gate_service = gate_service
        self.is_processing = False
        self.last_processed_plate_number: Optional[str] = None

        self.logger = logging.getLogger(__name__)

    # def __start_recognition(self) -> Optional[str]:
    #     for attempt in range(self.MAX_RECOGNITION_ATTEMPTS):
    #         try:
    #             image = self.camera_service.capture_image(True)
    #             result = self.recognition_service.recognize(image)
    #             if result:
    #                 self.logger.info(f"Successful detected number: {result}")
    #                 return result
    #             self.logger.warning(f"Attempt {attempt + 1}/{self.MAX_RECOGNITION_ATTEMPTS}: number not detected")
    #         except Exception as e:
    #             self.logger.error(f"Attempt {attempt + 1}/{self.MAX_RECOGNITION_ATTEMPTS} failed: {str(e)}")
    #
    #     self.logger.error("All attempts failed.")
    #     return None

    def process(self, sensor_type: SensorType) -> bool:
        try:
            plate_number = "BBK2906"
            if not plate_number:
                self.logger.warning("Plate number not detected.")
                self.led_service.blink_led_red()
                self.is_processing = False
                return False

            self.last_processed_plate_number = plate_number
            self.logger.info(f"Processing plate number: {plate_number}, gate type: {self.gate_service.type}")

            if self.gate_service.type == GateType.IN:
                return self._process_entry(plate_number)
            else:
                return self._process_exit(plate_number)
        except Exception as e:
            self.logger.error(f"Error processing: {str(e)}")
            self.led_service.blink_led_red()
            return False
        finally:
            self.is_processing = False

    def _process_entry(self, plate_number: str) -> bool:
        try:
            self.external_api_service.prepare_session(plate_number)
            self.led_service.blink_led_green()
            self.gate_service.automatic_open()
            return True
        except Exception as e:
            self.logger.error(f"Error processing: {str(e)}")
            self.led_service.blink_led_red()
            return False

    def _process_exit(self, plate_number: str) -> bool:
        try:
            self.external_api_service.prepare_end(plate_number)
            self.led_service.blink_led_green()
            self.gate_service.manually_open()
            return True
        except Exception as e:
            self.logger.error(f"Error preparing end session: {str(e)}")
            self.led_service.blink_led_red()
            return False

    def object_left(self) -> None:
        if not self.last_processed_plate_number:
            self.logger.warning("Don't know plate number.")
            return

        try:
            if self.gate_service.type == GateType.IN:
                self.logger.info(f"Session successful started: {self.last_processed_plate_number}")
                self.external_api_service.start_session(self.last_processed_plate_number)
                self.gate_service.automatic_close()
            else:
                self.logger.info(f"Session ending: {self.last_processed_plate_number}")
                self.external_api_service.stop_session(self.last_processed_plate_number)
                self.gate_service.automatic_close()
        except Exception as e:
            self.logger.error(f"Unexpected error: {str(e)}")
