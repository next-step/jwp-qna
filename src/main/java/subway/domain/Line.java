package subway.domain;

import javax.persistence.*;

@Entity
@Table(name = "line")
public class Line {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected Line() {
    }

    public Line(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void changeId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
