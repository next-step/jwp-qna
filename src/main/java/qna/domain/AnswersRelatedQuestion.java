package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Embeddable
public class AnswersRelatedQuestion {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    List<Answer> answers = new ArrayList<>();

    public AnswersRelatedQuestion() {
    }

    public List<Answer> getValue() {
        return answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> deleteRelatedAnswersAndCreateDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new LinkedList<>();

        for (Answer answer : answers) {
            deleteHistories.add(answer.deleteRelatedAnswerAndCreateDeleteHistory(loginUser));
        }
        return deleteHistories;
    }
}
