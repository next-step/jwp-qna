package qna.domain;

import qna.CannotDeleteException;

import java.util.List;

public class Answers {

    private List<Answer> answers;

    public static Answers from(List<Answer> answerList) {
        return new Answers(answerList);
    }

    public Answers(List<Answer> answerList) {
        this.answers = answerList;
    }

    public void deleteAll(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.delete(loginUser);
        }
    }

    public List<Answer> getAnswers() {
        return answers;
    }

}
