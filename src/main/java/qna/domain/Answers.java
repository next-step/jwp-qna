package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    protected Answers() {

    }

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private final List<Answer> answers = new ArrayList<>();
    
    public DeleteHistories deleteAll(User loginUser) {
        List<Answer> deletableAnswers = findDeletableAnswers();
        DeleteHistories deleteHistories = new DeleteHistories();
        deletableAnswers.stream()
                .map(answer -> answer.delete(loginUser))
                .forEach(deleteHistories::add);
        return deleteHistories;
    }

    public void add(Answer answer) {
        this.answers.add(answer);
    }

    public boolean contains(Answer answer) {
        return this.answers.contains(answer);
    }

    private List<Answer> findDeletableAnswers() {
        return answers.stream().filter(ans -> !ans.isDeleted()).collect(Collectors.toList());
    }


}
