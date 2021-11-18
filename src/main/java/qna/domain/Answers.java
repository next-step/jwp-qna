package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public List<Answer> getValue() {
        return answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser, LocalDateTime deletedTime) {
        List<DeleteHistory> deleteHistories = new LinkedList<>();

        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser, deletedTime));
        }
        return deleteHistories;
    }
}
