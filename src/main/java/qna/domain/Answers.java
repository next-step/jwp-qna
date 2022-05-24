package qna.domain;

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

    public List<DeleteHistory> delete(User deleteUser) {
        List<DeleteHistory> history = new ArrayList<>();
        for (Answer answer : list) {
            history.add(answer.delete(deleteUser));
        }
        return history;
    }

    public List<Answer> getList() {
        return list;
    }
}
