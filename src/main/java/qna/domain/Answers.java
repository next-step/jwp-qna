package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }

    public List<DeleteHistory> removeAll(User loginUser, LocalDateTime localDateTime) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.delete(loginUser);
            deleteHistories.add(DeleteHistory.ofAnswer(answer.getId(), loginUser, localDateTime));
        }
        return deleteHistories;
    }
}
