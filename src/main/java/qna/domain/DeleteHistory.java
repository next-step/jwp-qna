package qna.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DeleteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Content content;

    @Column(length = 6)
    private LocalDateTime createDate = LocalDateTime.now();

    public static DeleteHistory ofContent(Content content) {
        return new DeleteHistory(content, LocalDateTime.now());
    }

    private DeleteHistory(Content content, LocalDateTime createDate) {
        this.content = content;
        this.createDate = createDate;
    }

    protected DeleteHistory() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteHistory that = (DeleteHistory)o;
        return Objects.equals(id, that.id) &&
            Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeleteHistory{");
        sb.append("id=").append(id);
        sb.append(", content=").append(content);
        sb.append(", createDate=").append(createDate);
        sb.append('}');
        return sb.toString();
    }
}
