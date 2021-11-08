package qna.domain;

import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.List;

public class Answers {

    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void checkIsOwner(User writer) throws CannotDeleteException {
        for (Answer answer : answers) {
            checkIsOwner(writer, answer);
        }
    }

    private void checkIsOwner(User writer, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public void delete(List<DeleteHistory> deleteHistories) {
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(DeleteHistory.answer(answer.getId(), answer.getWriter()));
        }
    }
}
