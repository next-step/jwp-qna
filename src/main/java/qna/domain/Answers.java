package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer){
        this.answers.add(answer);
    }

    public List<Answer> answers() {
        return answers;
    }

    public List<DeleteHistory> delete(){
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for(Answer answer: this.answers){
            deleteHistories.add(answer.delete());
        }
        return deleteHistories;
    }

    public boolean existsOtherOwnedAnswer(User writer){
        return this.answers.stream().anyMatch(answer -> !answer.isOwner(writer) && !answer.isDeleted());
    }
}
