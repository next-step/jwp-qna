package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answerList = new ArrayList<>();

    protected Answers() {

    }

    public Answers(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public void addAnswer(Answer answer) {
        this.answerList.add(answer);
    }

    public List<Answer> nonDeletedAnswers() {
        return answerList.stream()
                .filter(answer -> !answer.isDeleted())
                .collect(Collectors.toList());
    }

    public List<DeleteHistory> deleteAnswers(final User loginUser) {
        List<Answer> nonDeletedAnswers = nonDeletedAnswers();

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : nonDeletedAnswers) {
            deleteHistories.add(answer.deleteAnswer(loginUser));
        }
        return deleteHistories;
    }
}
