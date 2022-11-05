package qna.domain;

import qna.CannotDeleteException;

import java.util.List;
import java.util.Objects;

public class AllWriterIsSameRule implements DeleteCheckRule {
    @Override
    public boolean deletable(User loginUser, List<Answer> answers) throws CannotDeleteException {
        if (!validInput(loginUser, answers))
            return false;

        if (!checkAllWriterIsSame(loginUser, answers)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }

        return true;
    }

    private boolean checkAllWriterIsSame(User loginUser, List<Answer> answers) {
        return answers.stream().allMatch(answer -> answer.isOwner(loginUser));
    }
}
