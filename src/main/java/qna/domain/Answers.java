package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<Answer> getList() {
        return list;
    }
}
