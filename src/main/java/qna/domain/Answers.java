package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<Answer>();
    
    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }
    
    public DeleteHistories delete() {
        DeleteHistories deleteHistories = new DeleteHistories();
        answers.forEach(answer -> deleteHistories.add(answer.delete()));
        return deleteHistories;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
    
    public void checkDeletableByQuestion(User writer) throws CannotDeleteException {
        if (countAnswers() != 0 && !checkWriter(writer)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }
    
    private boolean checkWriter(User writer) {
        return answers.stream().allMatch(answer -> answer.isSameWriter(writer));
    }
    
    public int countAnswers() {
        return answers.size();
    }

}
