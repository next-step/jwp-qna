package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers){
        this.answers = answers;
    }

    public List<Answer> deleteAnswers(User loginUser){
        List<Answer> deletedAnswers = new ArrayList<>();
        List<Answer> undeletedAnswers = answers.stream().filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList());
        for (Answer answer : undeletedAnswers) {
            deletedAnswers.add(answer.delete(loginUser));
        }
        return deletedAnswers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers(){
        return Collections.unmodifiableList(answers);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }
}
