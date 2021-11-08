package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.Assert;
import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> list = new ArrayList<>();

    protected Answers() {
    }

    private Answers(List<Answer> list) {
        this.list = list;
    }

    static Answers from(List<Answer> list) {
        if (list == null) {
            return create();
        }
        return new Answers(list);
    }

    static Answers create() {
        return from(new ArrayList<>());
    }

    void add(Answer answer) {
        Assert.notNull(answer, "added 'answer' must not be null");
        this.list.add(answer);
    }

    List<DeleteHistory> delete(User user) throws CannotDeleteException {
        validateListOwner(user);
        return deleteAnswers();
    }

    public boolean contains(Answer answer) {
        return list.contains(answer);
    }

    private List<DeleteHistory> deleteAnswers() {
        List<DeleteHistory> histories = new ArrayList<>();
        for (Answer answer : list) {
            histories.add(answer.delete());
        }
        return histories;
    }

    private void validateListOwner(User user) throws CannotDeleteException {
        for (Answer answer : list) {
            validateOwner(answer, user);
        }
    }

    private void validateOwner(Answer answer, User user) throws CannotDeleteException {
        if (answer.isNotOwner(user)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
}
