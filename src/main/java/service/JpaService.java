package service;

import domain.*;

import domain.exceptions.CarNotAvailableException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public record JpaService(EntityManagerFactory entityManagerFactory) implements Service {

    @Override
    public <T> T save(T toSave) {
        if (toSave.getClass() == Rental.class) {
            Rental rental = (Rental) toSave;
            if (!isCarAvailableForRental(rental)) throw new CarNotAvailableException();
        }
        var em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            toSave = em.merge(toSave);
            em.getTransaction().commit();
            return toSave;
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
            return Optional.ofNullable(em.find(Rental.class, id));
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
        if (rental.getReturnStation() != null) throw new IllegalArgumentException();

        var em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();

            //Setzen Finished-Felder
            rental.setReturnStation(station);
            rental.setDrivenKm(drivenKm);
            rental.setEnd(LocalDateTime.now());
            //Meilen und Station ändern
            rental.getCar().addMiles(drivenKm);
            rental.getCar().setLocation(station);

            em.merge(rental);

            em.getTransaction().commit();
            return rental;
        } finally {
            em.close();
        }
    }

    private boolean isCarOngoingRetail(Car car) {
        var em = entityManagerFactory.createEntityManager();
        try {
            em.createQuery("""
                            select rental 
                            from Rental rental 
                            where rental.end is null and rental.car = :car""")
                    .setParameter("car", car)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } finally {
            em.close();
        }
    }

    private boolean isCarAvailableForRental(Rental rental) {
        if (isCarOngoingRetail(rental.getCar())) return false;

        var em = entityManagerFactory.createEntityManager();
        try {
            em.createQuery("""
                            select rental from Rental rental
                            where :carToBorrow = rental.car
                            and rental.end is not null
                            and (:beginDate between rental.beginning and rental.end 
                                or :endDate between rental.beginning and rental.end
                                or :beginDate < rental.beginning and :endDate > rental.end)
                            """)
                    .setParameter("carToBorrow", rental.getCar())
                    .setParameter("beginDate", rental.getBeginning())
                    .setParameter("endDate", rental.getEnd())
                    .getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        } finally {
            em.close();
        }
    }
}