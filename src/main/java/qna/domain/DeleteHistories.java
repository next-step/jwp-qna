package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;

@Embeddable
public class DeleteHistories {

    private final List<DeleteHistory> deleteHistories;

    private DeleteHistories(final List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public List<DeleteHistory> toList() {
        return Collections.unmodifiableList(this.deleteHistories);
    }

    public static class Builder {
        private final List<DeleteHistory> deleteHistories = new ArrayList<>();

        public Builder addQuestion(final Question question) {
            this.deleteHistories.add(new DeleteHistory(
                ContentType.QUESTION, question.getId(), question.getWriter(), LocalDateTime.now())
            );

            return this;
        }

        public Builder addAnswers(final Answers answers) {
            answers.toList().forEach(this::addAnswer);

            return this;
        }

        private void addAnswer(final Answer answer) {
            this.deleteHistories.add(new DeleteHistory(
                ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now())
            );
        }

        public DeleteHistories build() {
            return new DeleteHistories(this.deleteHistories);
        }
    }
}
