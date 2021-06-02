package qna.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Questions {

    @OneToMany(mappedBy = "writer")
    private List<Question> questions = new ArrayList<>();

    protected Questions() {
    }

    public void add(Question question) {
        this.questions.add(question);
    }

    public List<Question> getQuestions(Status status) {
        return questions.stream()
            .filter(question -> question.isDeleted() == status.isDeleted())
            .collect(Collectors.toList());
    }

}
