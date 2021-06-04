package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public boolean isEmpty() {
        return answers.size() == 0;
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public boolean isSameSize(int size) {
        return answers.size() == size;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public boolean isAllSameWriter(User loginUser) {
        boolean anyMatch = false;
        for (Answer answer : answers) {
            anyMatch = answer.isWriterUser(loginUser);
        }
        return anyMatch;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Answers answers1 = (Answers) object;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
