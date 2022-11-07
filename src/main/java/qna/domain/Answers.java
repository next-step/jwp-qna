package qna.domain;

import java.util.List;

public class Answers {
    private static final String NULL_MESSAGE = "질문목록이 없습니다";
    public final List<Answer> answers;

    public Answers(List<Answer> answers) {
        validateNull(answers);
        this.answers = answers;
    }

    private void validateNull(List<Answer> answers) {
        if (answers == null) throw new RuntimeException(NULL_MESSAGE);

    }
}
