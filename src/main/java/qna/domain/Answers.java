package qna.domain;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import javax.persistence.*;

@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public DeleteHistories delete(User loginUser, LocalDateTime localDateTime) {
        return DeleteHistories.from(
            answers.stream()
                .map(e -> e.delete(loginUser, localDateTime))
                .collect(Collectors.toList())
        );
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

}
