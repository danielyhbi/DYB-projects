from abc import ABC, abstractmethod


class Engine(ABC):
    """
    Engine interface for all other engines. has 1 abstract class on if service is needed.
    """

    def __init__(self) -> None:
        super().__init__()

    @abstractmethod
    def needs_service(self) -> bool:
        pass
