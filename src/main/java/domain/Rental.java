package domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.assertj.core.internal.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;
@Builder
@AllArgsConstructor
@Getter
@Setter


@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive
    private Double drivenKm;

    private LocalDateTime beginning;

    @Column(name = "endtime")
    private LocalDateTime end;

    @ManyToOne
    private Car car;

    @ManyToOne
    private Station rentalStation;

    @ManyToOne
    private Station returnStation;

    public Rental() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Rental rental = (Rental) o;
        return id != null && Objects.equals(id, rental.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @AssertTrue
    public boolean isBeginingBeforEnd(){
        if(end == null){
            return true;
        }else {
            return end.isAfter(beginning);
        }
    }

}
