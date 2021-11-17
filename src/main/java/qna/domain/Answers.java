package qna.domain;

import java.util.Collections;
import java.util.List;

public class Answers {

    private final List<Answer> answers;

    public Answers(List<Answer> answerGroup) {
        answers = answerGroup;
    }

    public int size() {
        return answers.size();
    }

    public void delete() {
        for (Answer answer : answers) {
            answer.delete();
        }
    }

    public List<Answer> getAnswerGroup() {
        return Collections.unmodifiableList(answers);
    }
}
