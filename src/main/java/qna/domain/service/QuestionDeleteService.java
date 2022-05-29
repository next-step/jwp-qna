package qna.domain.service;

import java.util.ArrayList;
import java.util.List;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryFactory;
import qna.domain.Question;
import qna.domain.User;
import qna.exception.CannotDeleteException;
import qna.exception.UnAuthorizedException;

public class QuestionDeleteService {
    public List<DeleteHistory> deleteQuestionByLoginUser(Question question, User user) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        try{
            question.delete(user);
            deleteHistories.add(DeleteHistoryFactory.createQuestionDeleteHistory(question));
            deleteHistories.addAll(deleteAnswers(question.getAnswers(), user));
        } catch (UnAuthorizedException e) {
            throw new CannotDeleteException(e.getMessage(), e);
        }
        return deleteHistories;
    }

    private List<DeleteHistory> deleteAnswers(List<Answer> answers, User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.delete(loginUser);
            deleteHistories.add(DeleteHistoryFactory.createAnswerDeleteHistory(answer));
        }
        return deleteHistories;
    }
}
