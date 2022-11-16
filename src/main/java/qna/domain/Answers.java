package qna.domain;

import org.hibernate.annotations.Where;
import org.hibernate.sql.Delete;
import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Embeddable
public class Answers {

    @OneToMany(mappedBy ="question")
    @Where(clause = "deleted = false")
    List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public DeleteHistories deleteAnswers() {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER,answer.getId(),answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public boolean checkWriterAndLoginUser(User loginUser) {
        return Stream.of(answers)
                .flatMap(Collection::stream)
                .anyMatch(o -> !o.isOwner(loginUser));
    }
}
