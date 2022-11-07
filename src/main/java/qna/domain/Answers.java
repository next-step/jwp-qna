package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public boolean isEmpty() {
        return answers.isEmpty();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        validAnswerWriter(loginUser);

        return answers.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }

    private void validAnswerWriter(User loginUser) {
        for (Answer answer : answers) {
            answer.isNotWriter(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
