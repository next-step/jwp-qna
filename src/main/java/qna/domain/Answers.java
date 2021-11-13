package qna.domain;

import java.util.Collections;
import java.util.List;

public class Answers {
    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = Collections.unmodifiableList(answers);
    }

    public void validateAnswersOwner(User owner) {
        for (Answer answer : answers) {
            answer.validateAnswerOwner(owner);
        }
    }
}
