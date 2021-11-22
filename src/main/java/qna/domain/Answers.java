package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public static Answers of(Answer answer) {
        final Answers answers = new Answers();
        answers.addAnswer(answer);
        return answers;
    }

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> deleteAnswers(User writer, LocalDateTime localDateTime) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for( Answer answer: answers ) {
            deleteHistories.add(answer.deleteAnswer(writer, localDateTime));
        }
        return deleteHistories;
    }

    boolean existAnswer() {
        return answers.size() > 0;
    }

    public List<Answer> getAnswers() {
        return this.answers;
    }

}
