package qna.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Where;

import static java.util.stream.Collectors.toList;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    @Where(clause = "deleted = 0")
    private final Set<Answer> answers = new HashSet<>();

    public Set<Answer> getAnswers() {
        return answers;
    }

    void add(Answer answer) {
        answers.add(answer);
    }

    DeleteHistory delete(Answer answer, LocalDateTime deleteTime) {
        answers.remove(answer);
        return answer.delete(deleteTime);
    }

    boolean hasOtherUserAnswer(User owner) {
        return answers.stream()
                      .anyMatch(answer -> !answer.isOwner(owner));
    }

    DeleteHistories deleteAnswers(LocalDateTime deleteTime) {

        DeleteHistories deleteHistories =
            new DeleteHistories(answers.stream()
                                       .map(answer -> answer.delete(deleteTime))
                                       .collect(toList()));

        answers.clear();
        return deleteHistories;
    }
}
