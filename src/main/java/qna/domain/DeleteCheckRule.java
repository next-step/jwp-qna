package qna.domain;

import qna.CannotDeleteException;

import java.util.List;
import java.util.Objects;

public interface DeleteCheckRule {
    boolean deletable(User loginUser, List<Answer> answers) throws CannotDeleteException;

    default boolean validInput(User loginUser, List<Answer> answers) {
        return !(Objects.isNull(loginUser) || loginUser.isGuestUser() || Objects.isNull(answers));
    }
}
