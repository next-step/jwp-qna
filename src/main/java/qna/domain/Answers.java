package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public List<DeleteHistory> delete(User loginUser) {
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .map(answer -> answer.deleteIfValid(loginUser))
                .collect(Collectors.toList());
    }
}
