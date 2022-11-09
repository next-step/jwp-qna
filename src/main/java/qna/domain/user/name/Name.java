package qna.domain.user.name;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;

@Embeddable
public class Name {

    private static final int LIMIT_LENGTH = 20;

    @Column(length = LIMIT_LENGTH, nullable = false)
    private String name;

    protected Name() {
    }

    public Name(String name) {
        NullAndEmptyValidator.getInstance().validate(name);
        LengthValidator.getInstance().validate(name, LIMIT_LENGTH);
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Name)) {
            return false;
        }
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getName() {
        return name;
    }
}
