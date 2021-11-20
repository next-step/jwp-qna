package qna.domain.deletehistory;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class DeleteTarget {

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    protected DeleteTarget() {
    }

    private DeleteTarget(Long contentId, ContentType contentType) {
        this.contentId = contentId;
        this.contentType = contentType;
    }

    public static DeleteTarget of(Long contentId, ContentType contentType) {
        return new DeleteTarget(contentId, contentType);
    }

    public ContentType getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeleteTarget that = (DeleteTarget) o;
        return contentType == that.contentType && Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId);
    }
}
