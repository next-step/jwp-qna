package qna.domain.entity.collection;

import qna.CannotDeleteException;
import qna.domain.entity.Answer;
import qna.domain.entity.User;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Embeddable
public class Answers  {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private final Set<Answer> answers = new HashSet<>();

    public DeleteHistories deleted(User deleter) throws CannotDeleteException {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answers) {
            deleteHistories.append(answer.deleted(deleter));
        }

        return deleteHistories;
    }

    public void add(final Answer answer) {
        answers.add(answer);
    }

    public int size() {
        return answers.size();
    }

    public boolean contains(final Answer answer) {
        return answers.contains(answer);
    }

    public boolean isOnlyOwner(User user) {
        return answers.stream().allMatch(x -> x.isOwner(user));
    }

    public boolean nonOnlyOwner(User user) {
        return isOnlyOwner(user) == false;
    }
}

