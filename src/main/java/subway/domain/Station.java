package subway.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

/**
 *
 */
@Entity
public class Station /*implements Persistable*/ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    public Station(String name){
        this.name = name;
    }

    public Station(Long id, String name){
        this.id = id;
        this.name = name;
    }

    public Station() {

    }

    public Long getId() {
        return id;
    }

//    @Override
//    public boolean isNew() {
//        return true;
//    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    @ManyToOne // (1)
    @JoinColumn(name = "line_id") // (2)
    private Line line; // (3)

    public void setLine(final Line line) { // (4)
        this.line = line;
//        line.addStation(this); //연관관계... 무한루프
        if(!line.getStations().contains(this)) {
            line.getStations().add(this);
        }
    }


    public Line getLine() {
        return line;
    }
}
