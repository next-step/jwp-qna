package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName : qna.domain
 * fileName : Answers
 * author : haedoang
 * date : 2021/11/13
 * description : 답변 일급 컬렉션
 */
@Embeddable
public class Answers {
    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public boolean hasAnswers() {
        return this.answers.size() > 0;
    }

    public List<Answer> deletedFalseAnswers() {
        return this.answers.stream().filter(answer -> !answer.isDeleted()).collect(Collectors.toList());
    }
}

