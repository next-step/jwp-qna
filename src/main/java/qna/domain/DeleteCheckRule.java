package qna.domain;

import java.util.List;

public interface DeleteCheckRule {
    boolean deletable(User loginUser, List<Answer> answers);
}
