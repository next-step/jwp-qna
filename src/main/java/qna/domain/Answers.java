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

    public Answers() {
        answers = new ArrayList<>();
    }

    private void validateNull(List<Answer> answers) {
        if (answers == null) throw new RuntimeException(NULL_MESSAGE);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public int size() {
        return this.answers.size();
    }

    public List<DeleteHistory> getDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList();
        for (Answer answer : answers) {
            answer.delete();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }

    public void validateOwner(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.validateOwner(loginUser);
        }
    }

    public void deleteAllAnswer() {
        for (Answer answer : answers) {
            answer.delete();
        }
    }

}
