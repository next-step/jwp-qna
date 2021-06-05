package qna.domain.wrappers;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ContentId {

    @Column(name = "content_id")
    private Long contentId;

    public ContentId() {
    }

    public ContentId(Long contentId) {
        this.contentId = contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentId contentId1 = (ContentId) o;
        return Objects.equals(contentId, contentId1.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId);
    }

    @Override
    public String toString() {
        return "contentId=" + contentId;
    }
}
