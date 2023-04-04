from abc import ABC, abstractmethod


class Serviceable(ABC):
    """
    This interface class will be implemented by others to determine if 
    the vehicle needs service
    """

    def __init__(self) -> None:
        pass

    @abstractmethod
    def needs_service(self) -> bool:
        """
        returns if a car/vehicle is serviceable.
        """
        pass