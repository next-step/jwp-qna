package qna.domain;

import java.util.List;

public class Answers {

    private final List answers;

    public Answers(List<Answer> answerGroup) {
        answers = answerGroup;
    }

    public int size() {
        return answers.size();
    }
}
