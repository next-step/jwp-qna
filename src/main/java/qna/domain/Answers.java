package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    List<Answer> answers;

    protected Answers() {
        answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers){
        this.answers = answers;
    }

    public void deleteAnswers(User loginUser){
        for (Answer answer : answers) {
            answer.delete(loginUser);
        }
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getUnmodifiableAnswers(){
        return Collections.unmodifiableList(answers);
    }
}
