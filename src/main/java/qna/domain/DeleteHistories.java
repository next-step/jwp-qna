package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

    public List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() {
    }

    public void add(Question question, Answers answers) {
        removeQuestion(question);
        removeAnswers(answers);
    }

    private void removeQuestion(Question question) {
        question.delete();
        add(DeleteHistoryContent.removeQuestion(question), question.getWriter());
    }

    private void removeAnswers(Answers answers) {
        answers.list().forEach(this::removeAnswer);
    }

    private void removeAnswer(Answer answer) {
        answer.delete();
        add(DeleteHistoryContent.removeAnswer(answer), answer.getWriter());
    }

    private void add(DeleteHistoryContent deleteHistoryContent, User writer) {
        this.deleteHistories.add(new DeleteHistory(deleteHistoryContent, writer));
    }

    public List<DeleteHistory> getDeleteHistories() {
        return deleteHistories;
    }

    @Override
    public String toString() {
        return "DeleteHistories{" +
                "deleteHistories=" + deleteHistories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistories that = (DeleteHistories) o;
        return Objects.equals(deleteHistories, that.deleteHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deleteHistories);
    }
}
