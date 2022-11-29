package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> values = new ArrayList<>();

    public void add(final Answer answer) {
        this.values.add(answer);
    }

    public List<DeleteHistory> deleteAll(final User user) {
        return this.values.stream()
                .filter(answer -> !answer.isDeleted())
                .map(answer -> this.deleteAnswer(user, answer))
                .collect(Collectors.toList());
    }

    private DeleteHistory deleteAnswer(User user, Answer answer) {
        try {
            return answer.delete(user);
        } catch (CannotDeleteException exception) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.values);
    }
}
