package qna.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {

    public static final String CANNOT_DELETE_SOMEONE_ELSE_ANSWER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.PERSIST)
    private final List<Answer> answers;

    public Answers() {
        answers = new ArrayList<>();
    }

    public Answers(Answer... answers) {
        this.answers = new ArrayList<>(Arrays.asList(answers));
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public DeleteHistories deleteAnswers(User loginUser) {
        try {
            return answers.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.collectingAndThen(Collectors.toList(), DeleteHistories::new));
        } catch (CannotDeleteException e) {
            throw new CannotDeleteException(CANNOT_DELETE_SOMEONE_ELSE_ANSWER);
        }
    }

    public List<Answer> undeletedAnswers() {
        return answers.stream()
            .filter(answer -> !answer.isDeleted())
            .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        answers.add(answer);
    }
}
