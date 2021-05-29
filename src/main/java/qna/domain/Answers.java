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

    public void add(Answer answer) {
        answers.add(answer);
    }

    public DeleteHistory remove(Answer answer) {
        answers.remove(answer);
        answer.delete();
        return DeleteHistory.of(answer, LocalDateTime.now());
    }

    public boolean hasOtherUserAnswer(User owner) {
        return answers.stream()
                      .anyMatch(answer -> !answer.isOwner(owner));
    }

    public List<DeleteHistory> deleteAnswers(LocalDateTime deleteTime) {

        List<DeleteHistory> deleteHistories =
            answers.stream()
                   .map(answer -> remove(answer, deleteTime))
                   .collect(toList());

        answers.clear();

        return deleteHistories;
    }

    private DeleteHistory remove(Answer answer, LocalDateTime deleteTime) {
        answer.delete();
        return DeleteHistory.of(answer, deleteTime);
    }
}
