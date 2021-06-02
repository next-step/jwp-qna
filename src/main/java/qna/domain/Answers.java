package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void checkAuthorityDeleteAnswers(User deleter) throws CannotDeleteException {
        for (Answer answer : answers) {
            answer.checkAuthorityDeleteAnswer(deleter);
        }
    }

    public List<DeleteHistory> executeDeleted() {
        List<DeleteHistory> deletedAnswers = new ArrayList<>();
        answers.forEach(answer -> deletedAnswers.add(answer.executeDeleted()));
        return deletedAnswers;
    }

    public Long countAnswerOfOwner(User owner) {
        return answers.stream()
                .filter(answer -> answer.isOwner(owner))
                .count();
    }
}
