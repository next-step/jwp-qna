package qna.repository.entity;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Answers {
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {}

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }
}
