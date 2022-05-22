package qna.question.domain;

import common.entity.BasicEntity;
import qna.question.exception.CannotDeleteException;
import qna.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Question extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    @Column(nullable = false)
    private boolean deleted = false;

    protected Question() {}

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public List<DeleteHistory> deleteQuestionWithRelatedAnswer(
            User loginUser, List<Answer> relatedAnswers
    ) throws CannotDeleteException {
        if (!isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        for (Answer relatedAnswer : relatedAnswers) {
            isAnswerPossibleDelete(loginUser, relatedAnswer);
        }

        return deleteAndReturnHistories(relatedAnswers);
    }

    private void isAnswerPossibleDelete(User loginUser, Answer relatedAnswer) throws CannotDeleteException {
        if (!relatedAnswer.isOwner(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    private List<DeleteHistory> deleteAndReturnHistories(List<Answer> relatedAnswers) {
        List<DeleteHistory> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        this.questionDelete();
        result.add(new DeleteHistory(ContentType.QUESTION, this.id, this.writer, now));
        for (Answer answer : relatedAnswers) {
            answer.answerDelete();
            result.add(new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter(), now));
        }

        return result;
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public Long getId() {
        return this.id;
    }

    public User getWriter() {
        return this.writer;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    private boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    private void questionDelete() {
        this.deleted = true;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writerId=" + writer +
                ", deleted=" + deleted +
                '}';
    }
}
