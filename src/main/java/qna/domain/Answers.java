package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public void deleteAnswers(User loginUser) {
        this.answers.forEach(it -> it.delete(loginUser));
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
