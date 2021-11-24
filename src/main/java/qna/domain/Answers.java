package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answerList;

    public Answers() {
        this.answerList = new ArrayList<>();
    }

    public void remove(Answer answer) {
        answerList.remove(answer);
    }

    public void add(Answer answer) {
        answerList.add(answer);
    }

    public int size(){
        return answerList.size();
    }

    public boolean isMyAnswer(User user) {
        return answerList.stream().allMatch(answer -> answer.isOwner(user));
    }

    public List<DeleteHistory> delete() {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for(Answer answer : answerList){
            deleteHistories.add(answer.delete(true));
        }
        return deleteHistories;
    }
}
