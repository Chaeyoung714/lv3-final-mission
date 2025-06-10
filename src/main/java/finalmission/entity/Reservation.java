package finalmission.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @Enumerated(value = EnumType.STRING)
    private MusicalTime musicalTime;

    @ManyToOne
    private Musical musical;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Seat seat;

    public Reservation(LocalDate date, MusicalTime musicalTime, Musical musical, Member member, Seat seat) {
        this.date = date;
        this.musicalTime = musicalTime;
        this.musical = musical;
        this.member = member;
        this.seat = seat;
    }

    protected Reservation() {
    }

    public boolean matchesMember(Member member) {
        return this.member.getId().equals(member.getId());
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

    public LocalDate getDate() {
        return date;
    }

    public MusicalTime getMusicalTime() {
        return musicalTime;
    }

    public Seat getSeat() {
        return seat;
    }
}
