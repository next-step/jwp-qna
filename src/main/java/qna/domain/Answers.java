package qna.domain;

import qna.exception.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    protected Answers() {
    }

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public void addAnswer(Answer answer) {
        answers.add(answer);
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

}
