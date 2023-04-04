from engine.engine_interface import Engine
from battery.battery_interface import Battery
from utils.serviceable import Serviceable


class Car(Serviceable):
    def __init__(self, engine: Engine, battery: Battery):
        super().__init__
        self.engine = engine
        self.battery = battery

    # override
    def needs_service(self) -> bool:
        """
        Determines if a car needs service by checking both engine and battery
        """
        return self.engine.needs_service or self.battery.needs_service
