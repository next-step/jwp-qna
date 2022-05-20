package qna.domain.collections;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.domain.Answer;
import qna.domain.User;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {

    private static final int NONE = 0;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void deleteAll(User questionWriter) throws CannotDeleteException {
        for (Answer answer : answers){
            answer.delete(questionWriter);
        }
    }
}
