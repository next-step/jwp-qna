package qna.domain;

import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteHistories {

    private final List<DeleteHistory> list;

    public DeleteHistories(List<DeleteHistory> list) {
        this.list = list;
    }

    public DeleteHistories(Long questionId, User writer, List<Answer> answers) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(new DeleteHistory(ContentType.QUESTION, questionId, writer, LocalDateTime.now()));
        deleteHistories.addAll(deleteAnswers(writer, answers));
        this.list = deleteHistories;
    }

    private List<DeleteHistory> deleteAnswers(User writer, List<Answer> answers) {
        return answers.stream().map(answer -> {
            try {
                return answer.deletedBy(writer);
            } catch (CannotDeleteException e) {
                e.printStackTrace();
                throw new IllegalArgumentException();
            }
        }).collect(Collectors.toList());
    }

    public List<DeleteHistory> toList() {
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "list=" + list +
                '}';
    }
}
