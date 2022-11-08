package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answerList = new ArrayList<>();

    public void add(final Answer answer) {
        this.answerList.add(answer);
    }

    public List<DeleteHistory> deleteAll(final User user) {
        return this.answerList.stream()
                .map(answer -> answer.delete(user))
                .collect(Collectors.toList());
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.answerList);
    }
}
