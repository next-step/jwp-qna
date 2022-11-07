package qna.domain;

import org.hibernate.annotations.Where;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private final List<Answer> answers = new ArrayList<>();

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public DeleteHistories deleteAll() {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            answer.changeDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }

    public boolean isIncludedFromOtherThan(final User loginUser) {
        return Stream.of(answers)
                .flatMap(Collection::stream)
                .anyMatch(o -> !o.isOwner(loginUser));
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
