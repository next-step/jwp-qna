package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    private List<DeleteHistory> deleteHistories;

    public DeleteHistories() {
        deleteHistories = new ArrayList<>();
    }

    public DeleteHistories(List<DeleteHistory> deleteHistories) {
        this.deleteHistories = deleteHistories;
    }

    public List<DeleteHistory> asList() {
        return Collections.unmodifiableList(deleteHistories);
    }

    public void add(Question question) {
        this.deleteHistories.add(new DeleteHistory(ContentType.QUESTION, question, LocalDateTime.now()));
        add(question.getAnswers());
    }

    public void add(Answers answers) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers.asList()) {
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer, LocalDateTime.now()));
        }

        this.deleteHistories.addAll(deleteHistories);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return deleteHistories.equals(that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
