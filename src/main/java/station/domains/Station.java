package station.domains;

import javax.persistence.*;


@Entity // 엔티 클래스임을 지정하며 테이블과 매핑된다.
@Table(name = "station") // 엔티티가 매핑될 테이블을 지정, 생략시 엔티티 클래스 이름과 같이 생성, 굳이 쓸필요 없음
public class Station {

    @Id // 직접 매핑해서 사용하는 경우
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성규칙
    private Long id;

    @Column(name = "name", nullable = false) // 컬럼의 이름을 이용하여 필드나 속성을 컬럼에 매핑
    private String name;

    @ManyToOne // 다대일(N:1) 관계라는 매핑정보
    @JoinColumn(name = "line_id") // 컬럼 이름과 외래키가 참조할 컬럼을 직접 지정하지 않는다면 굳이 선언하지 않아도 됨
    private Line line; // 지하철역 객체는 라인필드로 노선 객체와 연관 관계를 맺는다.

    public void setLine(final Line line) { // 연관 관계를 설정하는 메서드
        this.line = line;
    }

    protected Station() { // 매개변수가 없는 생성자
    }

    public Station(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void changeName(String afterName) {
        this.name = afterName;
    }

    public Line getLine() {
        return this.line;
    }
}
