package qna.domain.content;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ContentId {


    private Long contentId;

    protected ContentId() {
    }

    public ContentId(Long contentId) {
        this.contentId = contentId;
    }

    public static ContentId of(Long contentId) {
        return new ContentId(contentId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentId contents1 = (ContentId) o;
        return Objects.equals(contentId, contents1.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId);
    }

    @Override
    public String toString() {
        return "ContentId{" +
                "contentId='" + contentId + '\'' +
                '}';
    }
}
