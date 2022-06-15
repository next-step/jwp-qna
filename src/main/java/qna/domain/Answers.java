package qna.domain;

import java.util.List;
import java.util.stream.Collectors;
import qna.CannotDeleteException;

public class Answers {

    List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void deleteAll(User questionWriter) throws CannotDeleteException {
        if (containsNotEqualsWriter(questionWriter)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        for (Answer answer : answers) {
            answer.delete();
        }
    }

    public boolean containsNotEqualsWriter(User questionWriter) {
        return answers.stream()
            .map(it -> questionWriter.equals(it.getWriter()))
            .collect(Collectors.toSet())
            .contains(false);
    }
}
