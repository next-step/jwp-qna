package qna.domain;

import qna.exception.CannotDeleteException;
import qna.message.AnswerMessage;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers {

    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST)
    private List<Answer> answerItems;

    public Answers() {
        this.answerItems = new ArrayList<>();
    }

    public Answers(Answer... answers) {
        this.answerItems = new ArrayList<>(Arrays.asList(answers));
    }

    public Answers(List<Answer> answers) {
        this.answerItems = new ArrayList<>(answers);
    }

    public void add(Answer answer) {
        if(this.answerItems.contains(answer)) {
            return;
        }
        this.answerItems.add(answer);
    }

    public DeleteHistories deleteAll(User owner) throws CannotDeleteException {
        validateOwners(owner);
        List<DeleteHistory> deleteHistories = this.answerItems.stream()
                .map(Answer::delete)
                .collect(Collectors.toList());
        this.answerItems.clear();
        return new DeleteHistories(deleteHistories);
    }

    private void validateOwners(User owner) throws CannotDeleteException {
        if(!isAllMatchAnswersOwner(owner)) {
            throw new CannotDeleteException(AnswerMessage.ERROR_CAN_NOT_DELETE_IF_OWNER_NOT_EQUALS.message());
        }
    }

    private boolean isAllMatchAnswersOwner(User owner) {
        return this.answerItems.stream().allMatch(answer -> answer.isOwner(owner));
    }

    public int size() {
        return this.answerItems.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answers answers = (Answers) o;

        return answerItems.equals(answers.answerItems);
    }

    @Override
    public int hashCode() {
        return answerItems.hashCode();
    }
}
