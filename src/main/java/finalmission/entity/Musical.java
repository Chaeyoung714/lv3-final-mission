package finalmission.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Month;

@Entity
public class Musical {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int reservationMonth;

    private String title;

    private String description;

    public Musical(Month reservationMonth, String title, String description) {
        this.reservationMonth = reservationMonth.getValue();
        this.title = title;
        this.description = description;
    }

    protected Musical() {
    }

    public Long getId() {
        return id;
    }

    public int getReservationMonth() {
        return reservationMonth;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
