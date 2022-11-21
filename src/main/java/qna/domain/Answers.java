package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answerList = new ArrayList<>();

    public void add(Answer answer) {
        this.answerList.add(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(this.answerList);
    }

    public DeleteHistories deleteAll() {
        DeleteHistories deleteHistories = new DeleteHistories();
        for (Answer answer : answerList) {
            answer.delete(true);
            deleteHistories.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), LocalDateTime.now()));
        }
        return deleteHistories;
    }

    public boolean isIncludedFromOtherThan(final User loginUser) {
        return Stream.of(answerList)
            .flatMap(Collection::stream)
            .anyMatch(o -> !o.isOwner(loginUser));
    }
}
