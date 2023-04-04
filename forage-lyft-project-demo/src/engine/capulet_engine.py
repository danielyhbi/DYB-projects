from engine_interface import Engine


class CapuletEngine(Engine):
    """
    Class for a Capulet Engine (concrete)
    """

    def __init__(self, current_mileage: int, last_service_mileage: int):
        super().__init__()
        self.current_mileage = current_mileage
        self.last_service_mileage = last_service_mileage

    def get_current_mileage(self) -> int:
        return self.current_mileage

    def get_last_service_mileage(self) -> int:
        return self.last_service_mileage

    # override
    def needs_service(self) -> bool:
        """
        Engine needs service if there has been 30,000 more miles sice the last service time.
        """
        return self.current_mileage - self.last_service_mileage > 30000
