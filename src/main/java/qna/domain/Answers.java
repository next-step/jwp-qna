package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = LAZY)
    private final List<Answer> elements = new ArrayList<>();

    public List<Answer> elements() {
        return Collections.unmodifiableList(elements);
    }

    public void deleteAll(DeleteHistories deleteHistories, User loginUser) {
        validateMyAnswers(loginUser);

        for (Answer answer : elements) {
            deleteHistories.addHistory(answer.delete(loginUser));
        }
    }

    private void validateMyAnswers(User loginUser) {
        boolean owner = elements.stream()
                .allMatch(answer -> answer.isOwner(loginUser));

        if (!owner) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public void add(Answer answer) {
        elements.add(answer);
    }
}
