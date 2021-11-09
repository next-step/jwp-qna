package qna.domain.answer;

import qna.CannotDeleteException;
import qna.domain.deletehistory.DeleteHistories;
import qna.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answers() {

    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories deletedBy(User user) throws CannotDeleteException {
        return new DeleteHistories(getAnswers().stream()
                .map(answer -> answer.deletedBy(user))
                .collect(Collectors.toList()));
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
}
