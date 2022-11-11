package qna.domain;

import qna.constant.ContentType;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public boolean isAllDeleted() {
        return answers.stream()
                .allMatch(Answer::isDeleted);
    }

    public void validationDeleteAnswersRequestUser(User loginUser) {
        for (Answer answer : answers) {
            answer.checkedSameOwner(loginUser);
        }
    }

    public List<DeleteHistory> generateDeleteHistories() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.changeDeleted();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }
}
