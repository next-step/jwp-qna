package qna.domain;

import java.util.List;
import java.util.stream.Collectors;
import qna.UnAuthorizedException;

public class Answers {

    List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void deleteAll(User questionWriter) {
        if (containsNotEqualsWriter(questionWriter)) {
            throw new UnAuthorizedException();
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
