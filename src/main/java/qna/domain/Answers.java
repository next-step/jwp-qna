package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import qna.CannotDeleteException;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        validateDuplicate(answer);

        answers.add(answer);
    }

    private void validateDuplicate(Answer answer) {
        if (answers.contains(answer)) {
            throw new IllegalArgumentException("중복된 답변이 존재합니다.");
        }
    }

    public List<DeleteHistory> delete(User user) throws CannotDeleteException {
        return answers.stream()
            .map(answer -> answer.delete(user))
            .collect(Collectors.toList());
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
