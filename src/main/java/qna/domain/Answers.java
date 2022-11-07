package qna.domain;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public DeleteHistories delete(User user) {
        return deleteAll(user);
    }

    private DeleteHistories deleteAll(User user) {
        return this.answers.stream()
                .map(answer -> answer.delete(user))
                .collect(Collectors.collectingAndThen(toList(), DeleteHistories::new));
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
    }

    public void addAnswer(Answer answer) {
        if(!answers.contains(answer)) {
            answers.add(answer);
        }
    }

    public int answersCount() {
        return answers.size();
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }
}
