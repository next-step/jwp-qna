package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;
import qna.exception.CannotDeleteException;

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

    public DeleteHistories deleteAll(final User loginUser) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            answer.delete(loginUser);
            deleteHistories.add(
                    new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
