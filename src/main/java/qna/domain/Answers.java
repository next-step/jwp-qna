package qna.domain;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> values = new ArrayList<>();

    public void deleteAll(User writer) {
        this.values.forEach(answer -> {
            answer.delete(writer);
        });
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
            throw new IllegalArgumentException("등록 할 답변이 존재하지 않습니다.");
        }
    }
}
