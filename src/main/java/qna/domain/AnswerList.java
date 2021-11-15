package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;
import qna.exception.CannotDeleteException;
import qna.exception.ErrorMessages;

@Embeddable
public class AnswerList {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    public AnswerList() {
        answers = new ArrayList<>();
    }

    public AnswerList(List<Answer> answers) {
        this.answers = answers;
    }

    public void deleteAnswers(User loginUser, DeleteHistoryList deleteHistoryList)
        throws CannotDeleteException {
        for (Answer answer : answers) {
            validate(answer, loginUser);
            answer.setDeleted(true);
            deleteHistoryList.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser));
        }
    }

    private void validate(Answer answer, User loginUser) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException(ErrorMessages.ANSWER_OTHER_USER_CANNOT_DELETE);
        }
    }


    public void add(Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    public boolean contains(Answer target) {
        return answers.contains(target);
    }
}
