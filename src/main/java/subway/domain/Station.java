package subway.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity // (1) 이 클래스가 엔티티임을 드러낸다. ( id를 가진 클래스다 )
@Table(name = "station") // (2) 테이블 이름을 직접 지정해주고 싶을 경우 쓴다. 없다면 클래스 이름을 그대로 사용함
public class Station {
    @Id // (3) 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4) 우리가 직접 아이디를 입력받는다면 필요없음. 자동 생성이 되는 값을 쓰면 필요한데 여러 타입이 있음
    private Long id;

    @Column(name = "name", nullable = false) // (5) 이 필드에 대한 이름을 실제 데이터베이스와 매핑될 컬럼 이름과 지정. 똑같으면 굳이 명시안해도 됨, 여러 설정이 있다.
    // nullable 은 DB에서 NOT NULL, but 메모리상에서는 null 이 들어갈 수 있음
    private String name;

    @ManyToOne
    @JoinColumn(name = "line_id") // foreign key의 이름을 지정하는 것 (일반적으로 필드명 + _ + 엔티티 아이디 명 )
    private Line line;

    @OneToOne // (1)
    @JoinColumn(name = "line_station_id") // (2)
    private LineStation lineStation; // (3)

    protected Station() { // (6) 매개변수 없는 생성자 기본 필요. 엔티티 클래스에는 있어야 한다고 명시
        //private 로 해도 된다. 그러면 Lazy Loading 안됨
    }

    public Station (Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, LineStation lineStation) {
        this.name = name;
        this.lineStation = lineStation;
    }

    public void setLine(Line line) {
        this.line = line;
        line.getStations().add(this);
    }

    public Line getLine() {
        return line;
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
}
