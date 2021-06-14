package qna.domain;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> values = new ArrayList<>();

    public List<DeleteHistory> deleteAll(User writer) {
        return this.values.stream()
            .map(answer -> answer.delete(writer))
            .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        validateAnswer(answer);
        values.add(answer);
    }

    public List<Answer> values() {
        return unmodifiableList(values);
    }

    private void validateAnswer(Answer answer) {
        if (Objects.isNull(answer)) {
            throw new IllegalArgumentException("null은 답변으로 추가할 수 없습니다.");
        }
    }
}
