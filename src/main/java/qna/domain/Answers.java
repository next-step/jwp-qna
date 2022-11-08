package qna.domain;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    protected Answers() {

    }

    public DeleteHistories deleteAll(User loginUser) {
        List<Answer> deletableAnswers = findDeletableAnswers();

        return new DeleteHistories(
                deletableAnswers.stream()
                        .map(answer -> answer.delete(loginUser))
                        .collect(Collectors.toList()));
    }

    public void update(Answer answer) {
        this.answers.add(answer);
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    private List<Answer> findDeletableAnswers() {
        return answers.stream().filter(ans -> !ans.isDeleted()).collect(Collectors.toList());
    }
}
