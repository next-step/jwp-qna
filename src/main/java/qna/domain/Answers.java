package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    private static final String NULL_MESSAGE = "질문목록이 없습니다";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    public final List<Answer> answers;

    public Answers(List<Answer> answers) {
        validateNull(answers);
        this.answers = answers;
    }

    public Answers(){
        answers = new ArrayList<>();
    }

    public List<Answer> getAnswers() {
        return answers;
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

    public List<DeleteHistory> getDeleteHistorys() {
        List<DeleteHistory> deleteHistories = new ArrayList();
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
