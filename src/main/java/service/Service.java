package service;

import domain.*;

import java.util.*;

public interface Service {

    <T> T save(T toSave);

    List<Station> findAllStations();

    List<Car> findAllCars();

    List<Rental> findAllRentals();

    Optional<Rental> findRentalById(long id);

    Set<Car> findCarsStationedAt(Station station);

    Rental finish(Rental rental, Station station, double drivenKm);
}
