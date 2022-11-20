package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = false")
    private final List<Answer> answers = new ArrayList<>();

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public boolean isNotWrittenUserInAnswers(final User loginUser) {
        return answers.stream().anyMatch(answer -> !answer.isOwner(loginUser));
    }

    public DeleteHistories deleteAll() {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(
                    new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
