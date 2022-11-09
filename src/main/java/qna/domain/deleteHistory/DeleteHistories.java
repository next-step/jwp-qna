package qna.domain.deleteHistory;

import qna.domain.question.Question;
import qna.domain.answer.Answer;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static qna.domain.deleteHistory.DeleteHistory.ofAnswerDeletedHistory;
import static qna.domain.deleteHistory.DeleteHistory.ofQuestionDeletedHistory;


@Embeddable
public class DeleteHistories implements Iterable<DeleteHistory> {
    @ManyToMany
    private List<DeleteHistory> deleteHistories = new ArrayList<>();

    public DeleteHistories() {
    }

    public List<DeleteHistory> addDeleteQuestionHistory(Question question) {
        DeleteHistory.ofQuestionDeletedHistory(question);
        deleteHistories.add(ofQuestionDeletedHistory(question));
        addDeletedAnswerHistories(question.getAnswers());

        return getDeleteHistories();
    }

    public List<DeleteHistory> getDeleteHistories() {
        return Collections.unmodifiableList(deleteHistories);
    }



    private void addDeletedAnswerHistories(List<Answer> answers) {
        for (Answer answer : answers) {
            deleteHistories.add(ofAnswerDeletedHistory(answer));
        }
    }

    @Override
    public Iterator<DeleteHistory> iterator() {
        return deleteHistories.iterator();
    }
}
