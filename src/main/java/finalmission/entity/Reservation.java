package finalmission.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    private Time time;

    @ManyToOne
    private Musical musical;

    @ManyToOne
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private SeatGrade seatGrade;

    private int seatNumber;

    public Reservation(Musical musical, Member member, SeatGrade seatGrade, int seatNumber) {
        this.musical = musical;
        this.member = member;
        this.seatGrade = seatGrade;
        this.seatNumber = seatNumber;
    }

    protected Reservation() {
    }

    public Long getId() {
        return id;
    }

    public Musical getMusical() {
        return musical;
    }

    public Member getMember() {
        return member;
    }

    public SeatGrade getSeatGrade() {
        return seatGrade;
    }

    public int getSeatNumber() {
        return seatNumber;
    }
}
