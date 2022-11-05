package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    private List<Answer> answers;

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<DeleteHistory> deleteAll(User questionWriter) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            if (!answer.isOwner(questionWriter)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
            answer.delete();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriteBy(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
