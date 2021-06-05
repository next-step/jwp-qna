package qna.domain.wrappers;

import qna.domain.Answer;
import qna.domain.ContentType;
import qna.domain.DeleteHistory;
import qna.domain.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Answers {
    private static final ContentType ANSWER_CONTENT_TYPE = ContentType.ANSWER;

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers;

    public Answers() {
        this.answers = new ArrayList<>();
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public boolean isEmpty() {
        return answers.size() == 0;
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public boolean isAllSameWriter(User loginUser) {
        boolean anyMatch = false;
        for (Answer answer : answers) {
            anyMatch = answer.isWriterUser(loginUser);
        }
        return anyMatch;
    }

    public List<DeleteHistory> createDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(DeleteHistory.create(ANSWER_CONTENT_TYPE, answer.getId(), loginUser));
            answer.delete();
        }
        return deleteHistories;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Answers answers1 = (Answers) object;
        return Objects.equals(answers, answers1.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
