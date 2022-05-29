package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    public List<DeleteHistory> deleteAll(User loginUser, LocalDateTime deletedDate) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();

        for(Answer answer : answers) {
            addNotNullHistory(deleteHistories, answer.delete(loginUser, deletedDate));
        }

        return deleteHistories;
    }

    private void addNotNullHistory(List<DeleteHistory> deleteHistories, DeleteHistory deleteHistory) {
        if(deleteHistory != null) {
            deleteHistories.add(deleteHistory);
        }
    }
}
