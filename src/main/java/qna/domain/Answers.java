package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    @Where(clause = "deleted = false")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public boolean isOwner(User user) {
        return answers.stream().allMatch(answer -> answer.isOwner(user));
    }

    public void validateOwner(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public DeleteHistories delete(User user) {
        validateOwner(user);
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        List<Answer> targetList = new CopyOnWriteArrayList<>(answers);
        for (Answer answer : targetList) {
            deleteHistories.add(answer.delete(user));
        }
        return DeleteHistories.of(deleteHistories);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }
}
