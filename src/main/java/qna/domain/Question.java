package qna.domain;

import javax.persistence.*;

@Entity
@Table(name = "question")
public class Question extends DateTimeEntity{
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "delete", nullable = false)
    private boolean deleted = false;

    @Lob
    @Column(name = "contests")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", foreignKey = @ForeignKey(name = "fk_question_writer"))
    private User writer;

    public Question(String title, String contents) {
        this(null, title, contents);
    }

    public Question(Long id, String title, String contents) {
        super.setId(id);
        this.title = title;
        this.contents = contents;
    }

    protected Question() {
    }

    public Question writeBy(final User writer) {
        this.writer = writer;
        return this;
    }

    public boolean isOwner(final User writer) {
        return this.writer.equals(writer);
    }

    public void addAnswer(final Answer answer) {
        answer.toQuestion(this);
    }

    public User getWriter() {
        return writer;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", writer=" + writer +
                ", deleted=" + deleted +
                '}';
    }
}
