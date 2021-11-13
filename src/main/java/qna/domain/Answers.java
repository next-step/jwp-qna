package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

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
    
    public void delete() {
        answers.forEach(answer -> answer.delete());
        answers.clear();
    }
    
    public void delete(Answer answer) {
        answers.removeIf(ans -> ans.equals(answer));
    }

    public List<Answer> getAnswers() {
        return answers;
    }
    
    public boolean checkWriter(User writer) {
        return answers.stream().allMatch(answer -> answer.isSameWriter(writer));
    }

}
