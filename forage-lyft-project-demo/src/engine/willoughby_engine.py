from abc import ABC
from engine_interface import Engine

class WilloughbyEngine(Engine, ABC):
    """
    Class for a WilloughbyEngine Engine (concrete)
    """

    def __init__(self, current_mileage, last_service_mileage):
        super().__init__()
        self.current_mileage = current_mileage
        self.last_service_mileage = last_service_mileage

    def get_current_mileage(self) -> int:
        return self.current_mileage
    
    def get_last_service_mileage(self) -> int:
        return self.last_service_mileage

    # overwrite
    def needs_service(self) -> bool:
        """
        Engine needs service if there has been 60,000 more miles sice the last service time.
        """
        return self.current_mileage - self.last_service_mileage > 60000