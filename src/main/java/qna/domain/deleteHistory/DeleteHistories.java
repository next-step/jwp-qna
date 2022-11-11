package qna.domain.deleteHistory;

import qna.domain.answer.Answer;
import qna.domain.question.Question;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class DeleteHistories {
    @ManyToMany
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() {
    }

    public List<DeleteHistory> addDeleteQuestionHistory(Question question) {
        deleteHistories.add(question.createDeleteHistory());
        addDeletedAnswerHistories(question.getAnswers());
        return getDeleteHistories();
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }


    private void addDeletedAnswerHistories(List<Answer> answers) {
        answers.stream().forEach(answer -> {
            deleteHistories.add(answer.createDeleteHistory());
        });
    }
}
