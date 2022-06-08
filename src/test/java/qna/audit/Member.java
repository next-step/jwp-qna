package qna.audit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import qna.domain.BaseEntity;

@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;

    protected Member() {
    }

    public Member(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void changeName(String changedName) {
        this.name = changedName;
    }

    @Override
    public String toString() {
        return "Member{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", age=" + age + '\'' +
            ", createdAt=" + getCreatedAt() + '\'' +
            ", updatedAt=" + getUpdatedAt() +
            '}';
    }
}
