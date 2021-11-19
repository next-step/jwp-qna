package qna.domain;

import static java.time.LocalDateTime.*;
import static java.util.stream.Collectors.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static qna.domain.ContentType.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers implements Serializable {

    @OneToMany(fetch = LAZY, mappedBy = "question", cascade = ALL)
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answers(Answer... answers) {
        this.answers = Arrays.stream(answers).collect(toList());
    }

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public void addAnswers(List<Answer> answers, Question question) {
        this.answers = answers.stream()
                              .peek(answer -> answer.toQuestion(question))
                              .collect(toList());
    }

    public List<DeleteHistory> delete(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            isNotOwer(loginUser, answer);

            answer.setDeleted(true);
            deleteHistories.add(new DeleteHistory(ANSWER, answer.getQuestion(), loginUser, now()));
        }
        return deleteHistories;
    }

    public Answers excludeDeleteTrueAnswers() {
        List<Answer> excludeDeleteTrueAnswers = answers.stream()
                                                       .filter(answer -> !answer.isDeleted())
                                                       .collect(toList());
        return new Answers(excludeDeleteTrueAnswers);
    }

    private void isNotOwer(User loginUser, Answer answer) throws CannotDeleteException {
        if (!answer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answers answers1 = (Answers)o;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
