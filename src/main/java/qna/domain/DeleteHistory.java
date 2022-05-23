package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column
    private ContentType contentType;
    @Column
    private Long contentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writerId", referencedColumnName = "userId")
    private User writer;
    @Column
    private LocalDateTime createDate;

    protected DeleteHistory() {
    }

    public DeleteHistory(ContentType contentType, Long contentId, User writer) {
        this.contentType = contentType;
        this.contentId = contentId;
        this.writer = writer;
        this.createDate = LocalDateTime.now();
    }

    public static DeleteHistory create(Question question) {
        return new DeleteHistory(ContentType.QUESTION, question.getId(), question.getWriter());
    }

    public static DeleteHistory create(Answer answer) {
        return new DeleteHistory(ContentType.ANSWER, answer.getId(), answer.getWriter());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
                contentType == that.contentType &&
                Objects.equals(contentId, that.contentId) &&
                Objects.equals(writer, that.writer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contentType, contentId, writer);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
                "id=" + id +
                ", contentType=" + contentType +
                ", contentId=" + contentId +
                ", writer=" + writer +
                ", createDate=" + createDate +
                '}';
    }
}
