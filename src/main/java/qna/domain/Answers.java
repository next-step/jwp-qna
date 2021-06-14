package qna.domain;

import qna.CannotDeleteException;
import qna.message.ErrorMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public Answers() {}

    public int size() {
        return answers.size();
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser){
        return answers.stream()
                .filter(answer -> !answer.isDeleted())
                .map(answer -> answer.delete(loginUser))
                .collect(Collectors.toList());
    }
}