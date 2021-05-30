package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers() { }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<Answer> answers() {
        return this.answers;
    }

    public DeleteHistories deleteAllAndHistories() {
        return new DeleteHistories(answers.stream()
                                        .peek(answer -> answer.setDeleted(true))
                                        .map(answer -> answer.deleteHistory())
                                        .collect(Collectors.toList()));
    }

    public void validateProprietary(User loginUser) throws CannotDeleteException {
        if (answers.stream().anyMatch(answer -> !answer.isOwner(loginUser))) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public void add(Answer answer) {
        if (!answers.contains(answer)) {
            answers.add(answer);
        }
    }
}
