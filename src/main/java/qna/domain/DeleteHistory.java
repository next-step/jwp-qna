package qna.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private DeleteHistoryContent deleteHistoryContent;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_delete_history_to_user"))
    private User deletedBy;
    private LocalDateTime createDate = LocalDateTime.now();

    protected DeleteHistory() {
    }

    protected DeleteHistory(DeleteHistoryContent deleteHistoryContent, User deletedBy) {
        this.deleteHistoryContent = deleteHistoryContent;
        this.deletedBy = deletedBy;
    }

    public static DeleteHistory ofAnswer(Answer answer) {
        return new DeleteHistory(DeleteHistoryContent.ofAnswer(answer), answer.getWriter());
    }

    public static DeleteHistory ofQuestion(Question question) {
        return new DeleteHistory(DeleteHistoryContent.ofQuestion(question), question.getWriter());
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistory that = (DeleteHistory) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(deleteHistoryContent, that.deleteHistoryContent) &&
               Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deleteHistoryContent, deletedBy, createDate);
    }

    @Override
    public String toString() {
        return "DeleteHistory{" +
               "id=" + id +
               ", deleteHistoryContent=" + deleteHistoryContent +
               ", deletedBy=" + deletedBy +
               ", createDate=" + createDate +
               '}';
    }
}
