package qna.domain;

import java.util.ArrayList;
import java.util.List;
import qna.CannotDeleteException;

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
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
