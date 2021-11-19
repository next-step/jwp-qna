package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> deleteAnswers(User writer) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for( Answer answer: answers ) {
            deleteHistories.add(answer.deleteAnswer(writer));
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
