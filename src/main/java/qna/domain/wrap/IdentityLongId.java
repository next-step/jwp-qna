package qna.domain.wrap;

import java.util.Objects;

public class IdentityLongId {
    private Long id;

    public IdentityLongId(Long id) {
        this.id = id;
    }

    public Long toLong() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityLongId that = (IdentityLongId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
