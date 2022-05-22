package subway.domain;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity // (1)
//@Table(name = "station") // (2) 없다면 클래스 이름을 테이블명으로 사용
public class Station {
    @Id // (3)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4) 직접 ID를 생성하는 경우는 필요x
    private Long id;                                     //auto-increment 옵션떄문에 쓰기지연이 안된다(영속성 컨텍스트에서 관리하기위해 id값이 필요해서 insert를 먼저해서 id를 얻는다)

    @Column(name = "name", nullable = false) // (5)
    private String name;

    //s@JoinColumn(name = "line_id")
    @ManyToOne //N:1일때 N쪽이 연관관계의 주인(FK관리자)
    private Line line;

    public void setLine(Line line) {
        this.line = line;
        line.getStations().add(this); //addStation()으로하면 무한루프가 돈다.
    }

    protected Station() { // (6)
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public Line getLine() {
        return line;
    }
}
