import time
import RPi.GPIO as GPIO



class LedService:
    LED_PIN_RED = 19
    LED_PIN_GREEN = 16

    def __init__(self):
        self.is_green_enabled = False
        self.is_red_enabled = False

        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.LED_PIN_RED, GPIO.OUT, initial=GPIO.LOW)
        GPIO.setup(self.LED_PIN_GREEN, GPIO.OUT, initial=GPIO.LOW)

    def _set_led_state(self, pin, state_var_name, enable):
        current_state = getattr(self, state_var_name)
        if current_state == enable:
            return

        setattr(self, state_var_name, enable)
        GPIO.output(pin, GPIO.HIGH if enable else GPIO.LOW)

    def enable_red_led(self):
        self._set_led_state(self.LED_PIN_RED, 'is_red_enabled', True)

    def disable_red_led(self):
        self._set_led_state(self.LED_PIN_RED, 'is_red_enabled', False)

    def enable_green_led(self):
        self._set_led_state(self.LED_PIN_GREEN, 'is_green_enabled', True)

    def disable_green_led(self):
        self._set_led_state(self.LED_PIN_GREEN, 'is_green_enabled', False)

    def blink_led_red(self, count=5, delay=0.5, initial_state=GPIO.LOW):
        for _ in range(count):
            self.enable_red_led()
            time.sleep(delay)
            self.disable_red_led()
            time.sleep(delay)

    def blink_led_green(self, count=5, delay=0.5, initial_state=GPIO.LOW):
        self.enable_green_led()
        time.sleep(delay)
        self.disable_green_led()
        time.sleep(delay)

    def cleanup(self):
        GPIO.cleanup()
