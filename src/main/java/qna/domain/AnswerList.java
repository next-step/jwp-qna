package qna.domain;

import java.util.ArrayList;
import java.util.List;
import qna.exception.CannotDeleteException;
import qna.exception.ErrorMessages;

public class AnswerList {

    private List<Answer> answers;

    public AnswerList() {
        answers = new ArrayList<>();
    }

    public AnswerList(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public int size() {
        return this.answers.size();
    }

    public void deleted(User loginUser, DeleteHistoryList deleteHistoryList)
        throws CannotDeleteException {
        for (Answer answer : answers) {
            validate(answer, loginUser);
            answer.setDeleted(true);
            deleteHistoryList.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser));
        }
    }

    private void validate(Answer answer, User loginUser) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorMessages.ANSWER_OTHER_USER_CANNOT_DELETE);
        }
    }
}
