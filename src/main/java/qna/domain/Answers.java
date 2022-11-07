package qna.domain;

import qna.CannotDeleteException;

import java.util.List;

public class Answers {
    private static final String NULL_MESSAGE = "질문목록이 없습니다";
    public final List<Answer> answers;

    public Answers(List<Answer> answers) {
        validateNull(answers);
        this.answers = answers;
    }

    private void validateNull(List<Answer> answers) {
        if (answers == null) throw new RuntimeException(NULL_MESSAGE);
    }

    public void validateOwner(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
    }
}
