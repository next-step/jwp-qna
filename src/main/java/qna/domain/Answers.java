package qna.domain;

import java.util.*;
import java.util.stream.*;

import javax.persistence.*;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public List<DeleteHistory> delete(User loginUser) {
        return answers.stream()
            .map(e -> e.delete(loginUser))
            .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

}
