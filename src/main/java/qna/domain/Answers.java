package qna.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Answers {

    private static final String NULL_MESSAGE = "질문목록이 없습니다";
    private static final String OTHER_WRITER_MESSAGE = "다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public Answers(List<Answer> answers) {
        validateNull(answers);
        this.answers = answers;
    }

    public Answers() {
    }

    private void validateNull(List<Answer> answers) {
        if (answers == null) throw new RuntimeException(NULL_MESSAGE);
    }

    public void addAnswer(Answer answer) {
        this.answers.add(answer);
    }

    public int size() {
        return this.answers.size();
    }

    public List<DeleteHistory> deleteAllAnswer(User writer) {
        List<DeleteHistory> deleteHistories = new ArrayList();
        boolean isAllSameWriter = answers
                .stream()
                .allMatch(answer -> answer.isOwner(writer));
        if (!isAllSameWriter) {
            throw new RuntimeException(OTHER_WRITER_MESSAGE);
        }
        for (Answer answer : answers) {
            deleteHistories.add(answer.delete(writer));
        }
        return deleteHistories;
    }

}
