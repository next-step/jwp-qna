package qna.domain;

import java.util.Arrays;
import java.util.Objects;

public enum DeletedType {
    NO(false),
    YES(true);

    private final boolean state;

    DeletedType(boolean state) {
        this.state = state;
    }

    public static DeletedType valueOf(final boolean value) {
        return Arrays.stream(values())
                .filter(type -> Objects.equals(type.state, value))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    public boolean isDeleted() {
        return Objects.equals(DeletedType.YES, this);
    }
}
