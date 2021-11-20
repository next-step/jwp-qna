package qna.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    private final List<Answer> answers;

    public Answers() {
        this.answers = Collections.unmodifiableList(new ArrayList<>());
    }

    public Answers(List<Answer> answerList) {
        this.answers = Collections.unmodifiableList(answerList);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public Answers add(Answer answer) {
        List<Answer> newAnswers = new ArrayList<>();
        newAnswers.addAll(this.answers);
        newAnswers.add(answer);
        return new Answers(newAnswers);
    }
}
