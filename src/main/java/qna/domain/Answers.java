package qna.domain;

import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "question")
    List<Answer> answers = new ArrayList<>();

    protected Answers() {
    }

    public Answers(List<Answer> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public boolean contains(Answer answer) {
        return answers.contains(answer);
    }

    public DeleteHistories deleteAll(User writer) throws CannotDeleteException {
        validateWriter(writer);
        return DeleteHistories.create(answers.stream().map(Answer::delete).collect(Collectors.toList()));
    }

    private void validateWriter(User writer) throws CannotDeleteException {
        for (Answer answer : answers) {
            if (!answer.isOwner(writer)) {
                throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
            }
        }
    }
}
