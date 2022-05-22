package qna.question.domain;

import qna.question.exception.CannotDeleteException;
import qna.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RelatedAnswersByQuestion {
    List<Answer> answers = new ArrayList<>();

    public RelatedAnswersByQuestion(List<Answer> relatedAnswers) {
        answers.addAll(relatedAnswers);
    }

    public void deleteAndCreateHistory(List<DeleteHistory> histories, User loginUser) throws CannotDeleteException {
        LocalDateTime now = LocalDateTime.now();

        for (Answer answer : this.answers) {
            answer.answerDelete(loginUser);
            histories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), now));
        }
    }
}
