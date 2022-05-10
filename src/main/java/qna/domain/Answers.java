package qna.domain;

import qna.exception.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {

    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> addAnswer(Answer newAnswer) {
        this.answers.add(newAnswer);
        return answers;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public void validateDelete(User loginUser) throws Exception {
        for (Answer answer : answers) {
            answer.validateDelete(loginUser);
        }
    }

    public void deleteAll(User loginUser, List<DeleteHistory> deleteHistories) throws Exception {
        for (Answer answer : answers) {
            answer.delete(loginUser, deleteHistories);
        }
    }

}
