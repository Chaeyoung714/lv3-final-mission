package finalmission.domain;

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

    private int musicalMonth;

    private String title;

    private String description;

    public Musical(Month musicalMonth, String title, String description) {
        this.musicalMonth = musicalMonth.getValue();
        this.title = title;
        this.description = description;
    }

    protected Musical() {
    }

    public Long getId() {
        return id;
    }

    public int getMusicalMonth() {
        return musicalMonth;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
