package qna.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import qna.exceptions.CannotDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseCreatedAndUpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private String contents;
    @Column(nullable = false)
    private boolean deleted = false;
    @Column(length = 100, nullable = false)
    private String title;
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private final List<Answer> answers = new ArrayList<>();

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

    public boolean isOwner(User writer) {
        return this.writer.getId().equals(writer.getId());
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
        this.answers.add(answer);
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Question setDeleted(boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public DeleteResultDto delete(User loginUser) throws CannotDeleteException {
        if (writer != loginUser) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }
        for (final Answer answer : answers) {
            answer.delete(loginUser);
        }
        setDeleted(true);
        return new DeleteResultDto(this, answers);
    }

    @Getter
    @AllArgsConstructor
    public static class DeleteResultDto {
        private final Question question;
        private final List<Answer> answers;
    }

    public List<DeleteHistory> getDeleteHistories(User loginUser) {
        List<DeleteHistory> deleteHistories = new ArrayList<>(1 + answers.size());
        deleteHistories.add(new DeleteHistory(id, ContentType.QUESTION, loginUser));
        answers.forEach(answer -> deleteHistories.add(new DeleteHistory(answer.getId(), ContentType.ANSWER, loginUser)));
        return deleteHistories;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", contents=" + contents +
                ", deleted=" + deleted +
                ", title=" + title +
                ", writer=" + writer.getId() +
                ", answers=" + answers +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
