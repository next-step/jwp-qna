package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import qna.enumType.ContentType;

public class Answers {
    private List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addDeleteHistory(List<DeleteHistory> deleteHistories, User loginUser) {
        answers.stream()
                .peek(Answer::setDeleted)
                .map(answer -> new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser))
                .forEach(deleteHistories::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answers answers1 = (Answers) o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
