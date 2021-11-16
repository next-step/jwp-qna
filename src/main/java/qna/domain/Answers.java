package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    private static final String ANSWER_ERROR_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public void validateAnswer(User writer) {
        for (Answer answer : answers) {
            validateSame(writer, answer);
        }
    }

    private void validateSame(User writer, Answer answer) {
        if (!answer.isOwner(writer)) {
            throw new CannotDeleteException(Answers.ANSWER_ERROR_MESSAGE);
        }
    }

    public int getSize() {
        return answers.size();
    }

    public List<DeleteHistory> createDeleteHistories() {
        List<DeleteHistory> answerDeleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answerDeleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(),
                    LocalDateTime.now()));
        }
        return answerDeleteHistories;
    }

    public void delete(User writer) {
        validateAnswer(writer);
        for (Answer answer : answers) {
            answer.delete();
        }
    }
}
