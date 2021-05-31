package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class Answers implements Iterable<Answer> {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> deleteAll(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : this) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

    @Override
    public Iterator<Answer> iterator() {
        return answers.iterator();
    }
}
