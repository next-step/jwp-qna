package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<Answer>();

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public List<DeleteHistory> deleteAll(User deleteUser) throws CannotDeleteException {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(deleteUser));
        }

        return deleteHistories;
    }
}
