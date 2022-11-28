package qna.domain;

import java.util.Collections;
import java.util.stream.Collectors;
import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void addAnswer(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public void validateAnswersWriter(User writer) throws CannotDeleteException {
        if (answers.stream().anyMatch(answer -> !answer.isOwner(writer))) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있는 경우 삭제할 수 없습니다");
        }
    }

    public List<DeleteHistory> delete() {
        return answers.stream()
            .map(answer -> answer.deleteAnswer())
            .collect(Collectors.toList());
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.answers);
    }
}