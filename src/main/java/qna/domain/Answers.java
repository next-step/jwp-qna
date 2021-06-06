package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answerList;

    public Answers(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public Answers() {
        this(new ArrayList<>());
    }

    public void add(Answer answer) {
        this.answerList.add(answer);
    }

    public boolean isDeletable(User loginUser) {
        return answerList.stream()
                .allMatch(answer -> answer.isOwner(loginUser));
    }

    public DeleteHistories delete(User loginUser) {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answerList) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }
}
