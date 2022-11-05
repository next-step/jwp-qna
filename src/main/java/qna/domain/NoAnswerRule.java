package qna.domain;

import java.util.List;
import java.util.Objects;

public class NoAnswerRule implements DeleteCheckRule{

    @Override
    public boolean deletable(User loginUser, List<Answer> answers) {
        if(Objects.isNull(loginUser) || loginUser.isGuestUser() || Objects.isNull(answers))
            return false;

        return answers.isEmpty();
    }
}
