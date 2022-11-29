package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public void deleteAnswers(DeleteHistories deleteHistories) {
        Stream<Answer> answerStream = answers.stream();
        answerStream.filter(answer -> !answer.isDeleted())
                .forEach(answer -> deleteHistories.add(answer.deleteAnswer()));
    }

    public boolean checkAnswersWriterMatch(User user) {
        Stream<Answer> answerStream = answers.stream();
        return answerStream.allMatch(answer -> answer.isOwner(user));
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
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
