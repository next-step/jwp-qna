package qna.domain.user.userid.factory;

import qna.domain.repository.UserRepository;
import qna.domain.user.userid.UserId;
import qna.domain.user.userid.validator.UserIdValidator;
import qna.domain.user.userid.validator.UserIdValidatorImpl;

public class UserIdFactoryImpl implements UserIdFactory {

    private static final int UNIQUE_THRESHOLD = 0;
    private final UserRepository repository;
    private final UserIdValidator validator;

    public UserIdFactoryImpl(UserRepository repository) {
        this.repository = repository;
        this.validator = new UserIdValidatorImpl();
    }

    @Override
    public UserId create(String userId) {
        validator.validate(userId);
        isNotUniqueThenThrow(userId);
        return new UserId(userId);
    }

    private void isNotUniqueThenThrow(String userId) {
        if (repository.countByUserId(new UserId(userId)) > UNIQUE_THRESHOLD) {
            throw new IllegalArgumentException();
        }
    }
}
