import configparser
import os
from sympy import false
from sympy.physics.quantum.gate import Gate
from enums.gate_status import GateStatus
from enums.gate_type import GateType


class Gate:

    def __init__(self):
        config = configparser.ConfigParser()
        config.read(os.path.join(os.path.dirname(__file__), 'config.ini'))
        self.isManualMode: bool = false
        self.type: GateType = config.get('app', 'type')
        self.name: str = config.get('app', 'name')
        self.id: str = None
        self.api_key: str = config.get('app', 'api_key')
        self.port: str = config.get('app', 'port')
        self.host: str = config.get('app', 'host')
        self.status: GateStatus = GateStatus.CLOSE

    def _open(self):
        self.status = GateStatus.OPEN
        # do something with IoT

    def _close(self):
        self.status = GateStatus.CLOSE
        # do something with IoT

    def manually_open(self):
        self._open()
        self.change_mode(True)

    def manually_close(self):
        self._close()
        self.change_mode(True)

    def automatic_open(self) -> bool:
        if self.isManualMode:
            return False
        else:
            self._open()
            return True

    def automatic_close(self) -> bool:
        if self.isManualMode:
            return False
        else:
            self._close()
            return True

    def change_mode(self, isManual: bool):
        if not isManual:
            self.isManualMode = isManual
            self._close()
