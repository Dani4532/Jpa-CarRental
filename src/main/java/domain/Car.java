package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor

@Entity
public class Car {

    @Id
    @Size(min = 4, max = 9)
    private String plate;

    @Positive
    private double mileage;

    private String model;

    @ManyToOne
    private Station location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Car car = (Car) o;
        return plate != null && Objects.equals(plate, car.plate);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "plate = " + plate + ", " +
                "mileage = " + mileage + ", " +
                "model = " + model + ")";
    }
}
