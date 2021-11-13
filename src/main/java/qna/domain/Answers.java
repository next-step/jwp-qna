package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void validateAnswersOwner(User owner) {
        for (Answer answer : answers) {
            answer.validateAnswerOwner(owner);
        }
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    public void deleteAll() {
        for (Answer answer : answers) {
            answer.setDeleted(true);
        }
    }

    public List<DeleteHistory> createDeleteHistories() {
        List<DeleteHistory> answerDeleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answerDeleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(),
                LocalDateTime.now()));
        }
        return answerDeleteHistories;
    }
}
