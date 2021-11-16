package qna.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import qna.common.exception.CannotDeleteException;

@Embeddable
public class Answers implements Serializable {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void add(Answer answer) {
        if (Objects.nonNull(answers)) {
            answers.remove(answer);
        }
        
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User questionWriter) {
        validWrittenByQuestionWriter(questionWriter);

        return answers.stream()
            .map(answer -> answer.delete(questionWriter))
            .collect(Collectors.toList());
    }

    private void validWrittenByQuestionWriter(User questionWriter) {
        long answersByQuestionWriterCount = answers.stream()
            .filter(answer -> answer.isOwner(questionWriter))
            .count();

        if (answersByQuestionWriterCount != answers.size()) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answers answers = (Answers) o;
        return Objects.equals(this.answers, answers.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answers);
    }
}
