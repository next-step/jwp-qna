package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy ="question")
    List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void deleteAnswers(User loginUser) throws CannotDeleteException {
        List<Answer> deletedFalseAnswers = answers
                .stream()
                .collect(Collectors.toList());
        for (Answer answer : deletedFalseAnswers) {
            answer.delete(loginUser);
        }
    }

    public void add(Answer answer) {
        answers.add(answer);
    }
}
