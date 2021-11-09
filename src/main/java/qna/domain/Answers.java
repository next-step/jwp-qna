package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void validateAnswers(User writer) {
        for (Answer answer : answers) {
            validateAnswer(writer, answer);
        }
    }

    public void deleteAnswers() {
        for (Answer answer : getAnswers()) {
            answer.delete();
        }
    }

    public List<DeleteHistory> getDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : getAnswers()) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter()));
        }
        return deleteHistories;
    }

    private void validateAnswer(User writer, Answer answer) {
        if (!answer.isOwner(writer)) {
            throw new CannotDeleteException(ErrorMessage.EXISTS_ANSWER_OF_OTHER);
        }
    }
}
