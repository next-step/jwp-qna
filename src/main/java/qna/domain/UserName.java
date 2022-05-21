package qna.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Embeddable
public class UserName {

    @Column(length = 20, nullable = false)
    private String name;

    protected UserName() {
    }

    public UserName(String name) {
        this.name = requireNonNull(name, "name");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserName userId = (UserName) o;
        return Objects.equals(this.name, userId.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
