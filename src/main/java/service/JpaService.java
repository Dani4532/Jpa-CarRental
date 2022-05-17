package service;

import domain.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.*;
import java.util.stream.Collectors;

public record JpaService(EntityManagerFactory entityManagerFactory) implements Service {


    @Override
    public Rental save(Rental rental) {
        var em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(rental);
            em.getTransaction().commit();
            return rental;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
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
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }

    }

    @Override
    public Car save(Car car) {
        var em = entityManagerFactory.createEntityManager();

        try {
            em.getTransaction().begin();
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
        try {
            return em.createQuery("""
                                select  station from Station station
                    """, Station.class).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Car> findAllCars() {
        var em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("""
                                    select car from Car car
                    """, Car.class).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Rental> findAllRentals() {
        var em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("""
                                    select rental from Rental rental
                    """, Rental.class).getResultList();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Rental> findRentalById(long id) {
        var em = entityManagerFactory.createEntityManager();
        try {
            var result = em.createQuery("""
                                    select rental from Rental rental where rental.id =: id
                    """, Rental.class).setParameter("id", id).getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            em.close();
        }


    }

    @Override
    public Set<Car> findCarsStationedAt(Station station) {
        var em = entityManagerFactory.createEntityManager();
        try {
            var result = em.createQuery("""
                                    select car from Car car where car.location =: station
                    """, Car.class).setParameter("station", station).getResultStream();
            var carSet = result.collect(Collectors.toSet());
            return carSet;
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Rental finish(Rental rental, Station station, double drivenKm) {
        return null;
    }
}