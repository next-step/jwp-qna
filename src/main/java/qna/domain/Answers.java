package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answerList) {
        this.answers = Collections.unmodifiableList(answerList);
    }

    public DeleteHistorys delete(User loginUser, LocalDateTime deleteDateTime) throws CannotDeleteException {
        return this.answers.stream()
            .map(answer -> answer.delete(loginUser, deleteDateTime))
            .collect(Collectors.collectingAndThen(
                Collectors.toList(), DeleteHistorys::new));
    }

    public Answers append(Answer answer) {
        return Stream.concat(Stream.of(answer), answers.stream())
            .collect(Collectors.collectingAndThen(Collectors.toList(), Answers::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answers answers1 = (Answers)o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
