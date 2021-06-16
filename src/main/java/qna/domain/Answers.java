package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public DeleteHistories delete(User writer) {
        return new DeleteHistories(answers.stream()
                .peek(answer -> answer.delete(writer))
                .map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter()))
                .collect(Collectors.toList())
        );
    }
}
