package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        vaildateOwner(user);

        return answers.stream()
            .map(answer -> answer.delete(user))
            .collect(Collectors.toList());
    }

    private void vaildateOwner(User user) {
        if (!isOwner(user)) {
            throw new CannotDeleteException("답변을 삭제할 권한이 없습니다.");
        }
    }

    private boolean isOwner(User user) {
        return answers.stream()
            .allMatch(answer-> answer.isOwner(user));
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
