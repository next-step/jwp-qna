package qna.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public List<DeleteHistory> delete(){
        return this.answers.stream()
                .map(answer -> answer.delete())
                .collect(Collectors.toList());
    }

    public List<Answer> getAnswers(){
        return answers;
    }

    public void add(Answer answer){
        this.answers.add(answer);
    }
}
