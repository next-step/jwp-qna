package qna.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private final List<Answer> answers;

    protected Answers() {
        this(new ArrayList<>());
    }

    private Answers(final List<Answer> answers) {
        this.answers = answers;
    }

    public void add(final Answer answer) {
        this.answers.add(answer);
    }

    public DeleteHistories deleteAll(final User loginUser) {
        final DeleteHistories deleteHistories = DeleteHistories.valueOf(answers.stream()
                .map(answer -> answer.delete(loginUser))
                .toArray(DeleteHistory[]::new));
        answers.clear();

        return deleteHistories;
    }

    public boolean contains(final Answer answer) {
        return answers.contains(answer);
    }
}
