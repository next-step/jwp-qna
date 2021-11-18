package qna.domain;

import qna.CannotDeleteException;

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

    public void delete() throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.delete(null);//TODO
        }
    }

    public List<Answer> getAnswerGroup() {
        return Collections.unmodifiableList(answers);
    }
}
