package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    public static final String NULL_OR_EMPTY_MESSAGE = "입력값이 null 이거나 빈 객체입니다.";

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {}

    public Answers(List<Answer> answers) {
        validate(answers);
        this.answers = answers;
    }

    public List<DeleteHistory> deleteAllByOwner(User loginUser) {
        return answers.stream()
                .map(wrap(answer -> answer.deleteByOwner(loginUser)))
                .collect(Collectors.toList());
    }

    private <T, R> Function<T, R> wrap(ExceptionFunction<T, R> function) {
        return (T input) -> {
            try {
                return function.apply(input);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private void validate(List<Answer> answers) {
        if (answers == null || answers.isEmpty()) {
            throw new IllegalArgumentException(NULL_OR_EMPTY_MESSAGE);
        }
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }
}
