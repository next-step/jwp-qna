package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;
import qna.CannotDeleteException;

@Embeddable
public class AnswerGroup {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected AnswerGroup() {
    }

    private AnswerGroup(List<Answer> answers) {
        this.answers = answers;
    }

    static AnswerGroup from(List<Answer> answers) {
        return new AnswerGroup(answers);
    }

    static AnswerGroup create() {
        return from(new ArrayList<>());
    }

    void add(Answer answer) {
        Assert.notNull(answer, "'answer' must not be null");
        this.answers.add(answer);
    }

    List<DeleteHistory> delete(User user) throws CannotDeleteException {
        validateListOwner(user);
        return deleteList();
    }

    private List<DeleteHistory> deleteList() {
        ArrayList<DeleteHistory> histories = new ArrayList<>();
        for (Answer answer : answers) {
            histories.add(answer.delete());
        }
        return histories;
    }

    private void validateListOwner(User user) throws CannotDeleteException {
        for (Answer answer : answers) {
            validateOwner(answer, user);
        }
    }

    private void validateOwner(Answer answer, User user) throws CannotDeleteException {
        if (answer.isNotOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
