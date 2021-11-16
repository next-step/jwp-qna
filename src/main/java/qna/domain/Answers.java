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

    public void delete(User loginUser) throws CannotDeleteException {
        validateOwner(loginUser);
        deleteAll();
    }

    private void deleteAll() {
        answers.forEach(Answer::delete);
    }

    private void validateOwner(User loginUser) throws CannotDeleteException {
        if(!isEntireAnswersOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 수 없습니다.");
        }
    }

    private boolean isEntireAnswersOwner(User loginUser) {
        return answers.stream().allMatch(answer -> answer.isOwner(loginUser));
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
