package qna.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answers {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "question_id", foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private List<Answer> answers = new ArrayList<>();

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<DeleteHistory> delete(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(loginUser));
        }
        return deleteHistories;
    }

}
