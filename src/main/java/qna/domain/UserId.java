package qna.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserId {

    @Column(name = "user_id", nullable = false, length = 20, unique = true)
    private String id;

    public UserId(final String id) {
        this.id = validId(id);
    }

    protected UserId() {
    }

    private String validId(final String id) {
        return Optional.ofNullable(id)
            .filter(string -> string.trim().length() > 0)
            .filter(string -> string.trim().length() <= 20)
            .map(String::trim)
            .orElseThrow(IllegalArgumentException::new);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserId userId = (UserId)o;
        return Objects.equals(id, userId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
