package qna.domain;


import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    public Answers() {
        answers = new ArrayList<>();
    }

    public boolean isIdenticalWriter(User loginUser) {

        long count = answers.stream()
                .filter(answer -> !answer.isOwner(loginUser))
                .count();

        return count == 0;
    }

    public DeleteHistories delete() {
        DeleteHistories answerDeleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            answerDeleteHistories.add(answer.delete());
        }
        return answerDeleteHistories;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public int getSize() {
        return answers.size();
    }

    public boolean isEmpty() {
        return answers.isEmpty();
    }
}
