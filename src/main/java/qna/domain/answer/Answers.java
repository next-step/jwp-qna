package qna.domain.answer;

import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OrderBy(value = "deleted DESC")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    private Answers(List<Answer> answers) {
        this.answers.addAll(answers);
    }

    public static Answers of() {
        return new Answers();
    }

    public static Answers of(List<Answer> answers) {
        return new Answers(answers);
    }

    public List<DeleteHistory> deleteAnswer(User loginUser) {
        return answers.stream()
                .map(answer -> answer.deleteByUser(loginUser))
                .collect(Collectors.toList());
    }

    public void remove(Answer answer) {
        this.answers.remove(answer);
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public int size() {
        return this.answers.size();
    }

    public Answer get(int index) {
        return answers.get(index);
    }
}
