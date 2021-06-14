package qna.domain;

import qna.CannotDeleteException;

import java.util.List;

public class Answers {
    List<Answer> answers;
    public Answers(List<Answer> answers) {
        this.answers = answers;
    }


    public void delete(User loginUser) throws CannotDeleteException {
        for (Answer answer: answers) {
            answer.delete(loginUser);
        }
    }
}
