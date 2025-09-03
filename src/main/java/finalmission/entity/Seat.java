package finalmission.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private SeatGrade grade;

    private int number;

    public Seat(SeatGrade grade, int number) {
        this.grade = grade;
        this.number = number;
    }

    protected Seat() {
    }

    public Long getId() {
        return id;
    }

    public SeatGrade getGrade() {
        return grade;
    }

    public int getNumber() {
        return number;
    }
}
