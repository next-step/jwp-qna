package qna.domain.collections;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.domain.Answer;

@Embeddable
public class Answers {

    private static final int NONE = 0;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public boolean hasAnswer(){
        return !hasNotAnswer();
    }

    private boolean hasNotAnswer() {
        int deletedCount = NONE;
        for (Answer answer : answers) {
            deletedCount = getNonDeletedCount(deletedCount, answer);
        }
        return deletedCount == answers.size();
    }

    private int getNonDeletedCount(int deletedCount, Answer answer) {
        if (answer.isDeleted()) {
            deletedCount++;
        }
        return deletedCount;
    }
}
