package qna.feedback.entity.lazy;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import qna.domain.BaseEntity;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "team_id",
        foreignKey = @ForeignKey(name = "FK_MEMBER_TO_TEAM")
    )
    private Team team;

    protected Member() {
    }

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Member(String name, int age, Team team) {
        this.name = name;
        this.age = age;
        this.team = team;
        addTeam();
    }

    public void addTeam() {
        this.team.getMembers().add(this);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void changeName(String changedName) {
        this.name = changedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
