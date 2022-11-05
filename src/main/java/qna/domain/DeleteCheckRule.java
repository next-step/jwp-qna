package qna.domain;

import qna.CannotDeleteException;

import java.util.List;

public interface DeleteCheckRule {
    boolean deletable(User loginUser, List<Answer> answers) throws CannotDeleteException;
}
