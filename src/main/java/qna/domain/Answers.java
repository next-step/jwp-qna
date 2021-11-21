package qna.domain;

import qna.ForbiddenException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    public static final String MESSAGE_NOT_OWNER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void validateNotOwnerAnswers(User loginUser) {
        for (Answer answer : answers) {
            checkNotOwnerAnswer(loginUser, answer);
        }
    }

    private void checkNotOwnerAnswer(User loginUser, Answer answer) {
        if (!answer.isOwner(loginUser)) {
            throw new ForbiddenException(MESSAGE_NOT_OWNER);
        }
    }

    public List<DeleteHistory> getDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }

    public void remove(Answer answer) {
        answers.remove(answer);
    }
}
