package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    protected Favorite() {
    }

    public void delete() {
        this.deleted = true;
    }

    @Override
    public String toString() {
        return "Favorite{" +
            "id=" + id + "," +
            "deleted=" + deleted +
            '}';
    }

}