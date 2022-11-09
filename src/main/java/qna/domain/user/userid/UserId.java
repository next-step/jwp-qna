package qna.domain.user.userid;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import qna.domain.repository.UserRepository;
import qna.domain.validate.string.LengthValidator;
import qna.domain.validate.string.NullAndEmptyValidator;

@Embeddable
public class UserId {

    private static final int LIMIT_LENGTH = 20;
    private static final int UNIQUE_THRESHOLD = 0;

    @Column(length = LIMIT_LENGTH, nullable = false, unique = true)
    private String userId;

    protected UserId() {
    }

    public UserId(String userId) {
        this(userId, null);
    }

    public UserId(String userId, UserRepository repository) {
        NullAndEmptyValidator.getInstance().validate(userId);
        LengthValidator.getInstance().validate(userId, LIMIT_LENGTH);
        isNotUniqueThenThrow(userId, repository);
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserId)) {
            return false;
        }
        UserId userId1 = (UserId) o;
        return Objects.equals(userId, userId1.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    public String getUserId() {
        return userId;
    }

    private void isNotUniqueThenThrow(String inputUserId, UserRepository repository) {
        if (repository != null && repository.countByUserId(new UserId(inputUserId)) > UNIQUE_THRESHOLD) {
            throw new IllegalArgumentException();
        }
    }
}
