package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answerList = new ArrayList<>();

    public List<DeleteHistory> delete(User loginUser) {
        return answerList.stream()
            .map(answer -> answer.delete(loginUser))
            .collect(Collectors.toList());
    }

    public void add(Answer answer) {
        this.answerList.add(answer);
    }

}