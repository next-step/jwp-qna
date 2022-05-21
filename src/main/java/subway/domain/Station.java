package subway.domain;

import javax.persistence.*;

@Entity // (1)
@Table(name = "station") // (2)
public class Station {
    @Id // (3)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // (4)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    //@JoinColumn(name = "line_id") // 자동으로 xx_id를 붙여준다.
    private Line line;

    protected Station() { // (6) 매개변수가 없는 기본 생성자가 있어야 한다. private은 레이지 로딩 안 됨
    }

    protected Station(String name) {
        this.name = name;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Line getLine() {
        return line;
    }
}
