package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answerList = new ArrayList<>();

    protected Answers() {

    }

    public List<Answer> getAnswerList() {
        return new ArrayList<>(answerList);
    }

    public void setAnswerList(List<Answer> answers) {
        for (Answer answer : answers) {
            answer.setQuestion(null);
        }
        answerList.addAll(answers);
    }

    public List<DeleteHistory> createDeleteHistories() {
        return answerList.stream()
                .map(Answer::createDeleteHistories)
                .collect(Collectors.toList());
    }
}
