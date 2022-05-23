package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Answers {
    private List<Answer> list;

    public Answers() {
        this.list = new ArrayList<>();
    }

    public Answers(List<Answer> list) {
        this.list = list;
    }

    public boolean isQuestionDeletePossible(User user) {
        return list.stream()
                .allMatch(answer -> answer.isOwner(user));
    }

    public void updateQuestion(Question update) {
        list.forEach(answer -> answer.toQuestion(update));
    }

    public List<DeleteHistory> delete(User user) {
        List<DeleteHistory> history = new ArrayList<>();
        for (Answer answer : list) {
            answer.setDeleted(true);
            history.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), LocalDateTime.now()).deleteBy(user));
        }
        return history;
    }

    public List<Answer> getList() {
        return list;
    }
}
