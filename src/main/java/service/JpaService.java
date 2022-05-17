package service;

import domain.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.*;

public record JpaService(EntityManagerFactory entityManagerFactory) implements Service {



    @Override
    public Rental save(Rental rental) {
        var em = entityManagerFactory.createEntityManager();
        var car = rental.getCar();
        var rentalStation = rental.getRentalStation();
        var returnStation = rental.getReturnStation();
        try{
            em.getTransaction().begin();
            em.merge(car);
            em.merge(rentalStation);
            em.merge(returnStation);
            em.persist(rental);
            em.getTransaction().commit();
            return rental;
        }catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public Station save(Station station) {
        var em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(station);
            em.getTransaction().commit();
            return station;
        }catch (Exception e){
            em.getTransaction().rollback();
            throw e;
        }finally {
            em.close();
        }

    }

    @Override
    public Car save(Car car) {
        var em = entityManagerFactory.createEntityManager();
        var location = car.getLocation();
        try {
            em.getTransaction().begin();
            em.merge(location);
            em.persist(car);
            em.getTransaction().commit();
            return car;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }

    }

    @Override
    public List<Station> findAllStations() {
        var em = entityManagerFactory.createEntityManager();
        try{
            return em.createQuery("""
            select  station from Station station
""",Station.class).getResultList();
        }catch (Exception e){
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public List<Car> findAllCars() {
        return null;
    }

    @Override
    public List<Rental> findAllRentals() {
        return null;
    }

    @Override
    public Optional<Rental> findRentalById(long id) {
        return Optional.empty();
    }

    @Override
    public Set<Car> findCarsStationedAt(Station station) {
        return null;
    }

    @Override
    public Rental finish(Rental rental, Station station, double drivenKm) {
        return null;
    }
}