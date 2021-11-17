package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserName {
    @Column(nullable = false, length = 20)
    private String name;

    protected UserName() {
    }

    public UserName(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserName otherUserName = (UserName) o;
        return Objects.equals(name, otherUserName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
