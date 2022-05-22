package qna.domain;


import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private List<Answer> values = new ArrayList<>();

    protected Answers() {
    }

    public void add(Answer answer) {
        values.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        return values.stream()
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }
}