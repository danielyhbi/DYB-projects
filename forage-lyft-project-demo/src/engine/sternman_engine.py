from abc import ABC
from engine_interface import Engine

class SternmanEngine(Engine, ABC):
    """
    Class for a SternmanEngine Engine (concrete)
    """

    def __init__(self, warning_light_on: bool) -> None:
        super().__init__()
        self.warning_light_on = warning_light_on


    # overwrite
    def needs_service(self) -> bool:
        """
        Engine needs service if wanring light is on.
        """
        return self.warning_light_on

