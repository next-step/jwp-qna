package qna.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import qna.exception.CannotDeleteException;

public class Answers {

    public static final String CANNOT_DELETE_SOMEONE_ELSE_ANSWER = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";
    private final List<Answer> answers;

    public Answers(Answer... answers) {
        this.answers = Arrays.asList(answers);
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public DeleteHistories deleteAnswers(User loginUser) throws CannotDeleteException {
        try {
            List<DeleteHistory> deleteHistories = answers.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
            return new DeleteHistories(deleteHistories);
        } catch (CannotDeleteException e) {
            throw new CannotDeleteException(CANNOT_DELETE_SOMEONE_ELSE_ANSWER);
        }
    }

    public List<Answer> undeletedAnswers() {
        return answers.stream()
            .filter(answer -> !answer.isDeleted())
            .collect(Collectors.toList());
    }
}
