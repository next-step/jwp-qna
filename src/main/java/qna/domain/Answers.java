package qna.domain;

import java.util.ArrayList;
import java.util.List;
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

    public boolean isOwner(User writer) {
        return answerList.stream()
                .allMatch(answer -> answer.isOwner(writer));
    }

    public List<DeleteHistory> deleteAnswers() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answerList) {
            deleteHistories.add(answer.deleteAnswer());
        }
        return deleteHistories;
    }
}
