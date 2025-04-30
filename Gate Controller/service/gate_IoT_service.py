import time
from enum import Enum
from typing import NoReturn
import RPi.GPIO as GPIO

SERVO_PIN = 18
PWM_FREQUENCY = 50
DUTY_BASE = 2
DUTY_FACTOR = 18
SERVO_DELAY = 0.1


class Position(Enum):
    OPEN = 90
    CLOSE = 0


class GateIoTService:

    def __init__(self) -> None:
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(SERVO_PIN, GPIO.OUT)

        self.pwm: GPIO.PWM = GPIO.PWM(SERVO_PIN, PWM_FREQUENCY)
        self.pwm_started: bool = False

        self.position: Position = Position.CLOSE
        self._set_angle(self.position)

    def _set_angle(self, angle: Position) -> None:
        duty = DUTY_BASE + (angle.value / DUTY_FACTOR)

        if not self.pwm_started:
            self.pwm.start(duty)
            self.pwm_started = True
        else:
            self.pwm.ChangeDutyCycle(duty)

        time.sleep(SERVO_DELAY)
        self.pwm.ChangeDutyCycle(0)
        self.position = angle

    def open(self) -> None:
        if self.position != Position.OPEN:
            self._set_angle(Position.OPEN)

    def close(self) -> None:
        if self.position != Position.CLOSE:
            self._set_angle(Position.CLOSE)

    def get_position(self) -> Position:
        return self.position

    def cleanup(self) -> NoReturn:
        self.pwm.stop()
        GPIO.cleanup()
