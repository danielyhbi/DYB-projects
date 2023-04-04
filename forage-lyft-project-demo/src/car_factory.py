from abc import ABC
from car import Car
import engine as Engine
import battery as Battery

class CarFactory:
    """
    Generate car objects using the factory design pattern
    """

    def __init__(self) -> None:
        pass

    def create_calliope(current_date: int, last_service_date: int, 
                        current_mileage: int, last_service_mileage: int) -> Car:
        return Car(
                Engine.CapuletEngine(current_mileage, last_service_mileage), 
                Battery.SpindlerBattery(current_date, last_service_date)
                )
    
    def create_glissade(current_date: int, last_service_date: int, 
                        current_mileage: int, last_service_mileage: int) -> Car:
        return Car(
                Engine.WilloughbyEngine(current_mileage, last_service_mileage), 
                Battery.SpindlerBattery(current_date, last_service_date)
                )
    
    def create_palindrome(current_date: int, last_service_date: int, 
                        warning_light_on: bool) -> Car:
        return Car(
                Engine.SternmanEngine(warning_light_on), 
                Battery.SpindlerBattery(current_date, last_service_date)
                )
    
    def create_rorschach(current_date: int, last_service_date: int, 
                        current_mileage: int, last_service_mileage: int) -> Car:
        return Car(
                Engine.WilloughbyEngine(current_mileage, last_service_mileage), 
                Battery.NubbinBattery(current_date, last_service_date)
                )
    
    def create_thovex(current_date: int, last_service_date: int, 
                        current_mileage: int, last_service_mileage: int) -> Car:
        return Car(
                Engine.CapuletEngine(current_mileage, last_service_mileage), 
                Battery.NubbinBattery(current_date, last_service_date)
                )
    