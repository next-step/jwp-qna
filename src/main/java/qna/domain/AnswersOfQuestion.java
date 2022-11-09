package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import qna.CannotDeleteException;

public class AnswersOfQuestion {
    List<Answer> answers;

    private AnswersOfQuestion(){}

    public static AnswersOfQuestion create(List<Answer> answers){
        AnswersOfQuestion answersOfQuestion = new AnswersOfQuestion();
        answersOfQuestion.answers = answers;

        return answersOfQuestion;
    }

    public void validateLoginUserIsOwner(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            if (!answer.isOwner(loginUser)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
    }

    public List<DeleteHistory> generateDeleteHistories(){
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return new ArrayList<>(deleteHistories);
    }
}
