package qna.domain.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import qna.domain.Answer;
import qna.domain.User;
import qna.exception.CannotDeleteException;

@Embeddable
public class Answers {

    private static final String DELETED_HISTORY_KEY = "DELETED_HISTORY";
    private static final Map<String,List<Answer>> ANSWERS_CACHE = new HashMap<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public void deleteAll(User questionWriter) throws CannotDeleteException {
        List<Answer> deleteAnswers = new ArrayList<>();
        for (Answer answer : answers) {
            addDeleteAnswers(deleteAnswers, answer);
        }
        for (Answer answer : deleteAnswers) {
            answer.delete(questionWriter);
        }
        ANSWERS_CACHE.put(DELETED_HISTORY_KEY,deleteAnswers);
    }

    private void addDeleteAnswers(List<Answer> needDeleteAnswers, Answer answer) {
        if (!answer.isDeleted()) {
            needDeleteAnswers.add(answer);
        }
    }

    public List<Answer> getDeletedAnswers() {
        return ANSWERS_CACHE.getOrDefault(DELETED_HISTORY_KEY, Collections.emptyList());
    }
}
