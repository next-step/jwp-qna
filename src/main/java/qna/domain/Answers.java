package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    protected Answers() {
    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();


    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> deleteAnswers(User writer) throws CannotDeleteException {
        List<DeleteHistory> deleteHistoryList = new ArrayList<>();

        for (Answer answer : answers) {
            deleteHistoryList.add(answer.delete(writer));
        }
        for(int i=0;i<deleteHistoryList.size();i++){
            System.out.println("zzzz"+deleteHistoryList.get(i));
        }
        return deleteHistoryList;
    }

    public void clear() {
        answers.clear();
    }
}