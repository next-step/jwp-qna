package qna.domain.answer;

import qna.CannotDeleteException;
import qna.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static qna.constant.Message.NOT_VALID_DELETE_QUESTION_WITH_ANSWER;

@Embeddable
public class Answers implements Iterable<Answer> {
    // FK는 Answer의 question 필드
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    @Override
    public Iterator<Answer> iterator() {
        return answers.iterator();
    }

    public void validateDeleteAnswer(User loginUser) throws CannotDeleteException {
        for (Answer answer : answers) {
            checkAnswerWriter(answer, loginUser);
        }
    }

    private void checkAnswerWriter(Answer answer, User loginUser) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException(NOT_VALID_DELETE_QUESTION_WITH_ANSWER);
        }
    }

    public void deleteAll() {
        for(Answer answer : answers) {
            answer.updateDeleted(true);
        }
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            // answer이 중복으로 담기지 않도록
            answers.add(answer);
        }
    }
}
