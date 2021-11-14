package qna.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import qna.common.exception.CannotDeleteException;

@Embeddable
public class Answers implements Serializable {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Answer> values = new ArrayList<>();

    protected Answers() {
    }

    protected Answers(List<Answer> answers) {
        this.values = answers;
    }

    public List<Answer> values() {
        return values;
    }

    public void add(Answer answer) {
        values.add(answer);
    }

    public List<DeleteHistory> delete(User questionWriter) {
        validWrittenByQuestionWriter(questionWriter);

        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : values) {
            deleteHistories.add(answer.delete(answer.getWriter()));
        }

        return deleteHistories;
    }

    private void validWrittenByQuestionWriter(User questionWriter) {
        long answersByQuestionWriterCount = values.stream()
            .filter(answer -> answer.isOwner(questionWriter))
            .count();

        if (answersByQuestionWriterCount != values.size()) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answers answers = (Answers)o;
        return Objects.equals(this.values, answers.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
