package qna.domain;


import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> values = new ArrayList<>();

    protected Answers() {
    }

    public void add(Long questionId, Answer answer) {
        validateNull(answer);
        validateAnswer(questionId, answer);
        validateDuplicate(answer);

        values.add(answer);
    }

    private void validateNull(Answer answer) {
        if (Objects.isNull(answer)) {
            throw new IllegalArgumentException("추가하려는 답변이 존재하지 않습니다.");
        }
    }

    private void validateAnswer(Long questionId, Answer answer) {
        if (!Objects.equals(questionId, answer.getQuestion().getId())) {
            throw new IllegalArgumentException("다른질문의 답변입니다.");
        }
    }

    private void validateDuplicate(Answer answer) {
        if (values.contains(answer)) {
            throw new IllegalArgumentException("중독된 답변입니다.");
        }
    }

    public List<DeleteHistory> delete(User loginUser) {
        return values.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }

    public List<Answer> getAnswers() {
        return values;
    }
}