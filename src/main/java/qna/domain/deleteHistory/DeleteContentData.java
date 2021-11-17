package qna.domain.deleteHistory;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class DeleteContentData {

    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(name = "content_id")
    private Long contentId;

    protected DeleteContentData() {
    }

    private DeleteContentData(Long contentId, ContentType contentType) {
        this.contentId = contentId;
        this.contentType = contentType;
    }

    public static DeleteContentData of(Long contentId, ContentType contentType) {
        return new DeleteContentData(contentId, contentType);
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
        DeleteContentData that = (DeleteContentData) o;
        return contentType == that.contentType && Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentType, contentId);
    }
}
