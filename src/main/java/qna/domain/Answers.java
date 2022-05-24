package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        if (this.answers.contains(answer)) {
            throw new IllegalArgumentException("이미 등록된 답변입니다.");
        }
        this.answers.add(answer);
    }

    public List<Answer> list() {
        return answers;
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    public List<DeleteHistory> deleteAnswers(User loginUser) {
        return this.answers.stream()
                .map(it -> it.delete(loginUser))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
