package qna.domain;

import qna.message.QuestionMessage;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Lob
    private String contents;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_question_user"))
    private User writer;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted = false;

    protected Question() {

    }

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        validateTitle(title);
        validateContents(contents);

        this.id = id;
        this.title = title;
        this.contents = contents;
    }

    private void validateTitle(String title) {
        if(Objects.isNull(title)) {
            throw new IllegalArgumentException(QuestionMessage.ERROR_TITLE_SHOULD_BE_NOT_NULL.message());
        }
    }

    private void validateContents(String contents) {
        if(Objects.isNull(contents)) {
            throw new IllegalArgumentException(QuestionMessage.ERROR_CONTENTS_SHOULD_BE_NOT_NULL.message());
        }
    }

    public Long getId() {
        return id;
    }

    public User writer() {
        return this.writer;
    }

    public Question writeBy(User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(Answer answer) {
        answer.toQuestion(this);
    }

    public void delete() {
        this.deleted = true;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(title, question.title) && Objects.equals(writer, question.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, writer);
    }
}
