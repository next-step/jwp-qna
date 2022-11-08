package qna.domain.answer;

import qna.domain.deletehistory.DeleteHistory;
import qna.domain.user.User;
import qna.exception.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Answers implements Iterable<Answer> {

    @OneToMany(mappedBy = "question"/*외래키는 Answer 객체에 question필드에서 관리하고 있음을 뜻함 */, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private final List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public List<DeleteHistory> deleteAll(User loginUser) {
        return answers.stream()
                .map(answer -> answer.deleteBy(loginUser))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Answer> iterator() {
        return answers.iterator();
    }

    public void add(Answer answer) {
        if(!isDuplicatedAnswer(answer)){
            answers.add(answer);
        }
    }

    public boolean isDuplicatedAnswer(Answer answer) {
        return answers.contains(answer);
    }

    public List<Answer> getAnswers() {
        return Collections.unmodifiableList(answers);
    }
}
