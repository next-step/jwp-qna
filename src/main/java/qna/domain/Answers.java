package qna.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answers {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validateOwner(loginUser);
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            answer.delete();
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), loginUser));
        }
        return deleteHistories;
    }

    private void validateOwner(User loginUser) throws CannotDeleteException {
        if(!isEntireAnswersOwner(loginUser)) {
            throw new CannotDeleteException("질문자와 답변자가 다른 경우 답변을 삭제할 수 없습니다.");
        }
    }

    private boolean isEntireAnswersOwner(User loginUser) {
        return answers.stream().allMatch(answer -> answer.isOwner(loginUser));
    }

}
