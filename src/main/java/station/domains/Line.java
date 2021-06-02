package station.domains;

import javax.persistence.*;

@Entity
@Table(name = "line")
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public Line(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
