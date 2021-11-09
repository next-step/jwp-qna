package qna.domain.deletehistory;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ContentId {

    private Long contentId;

    public ContentId(Long contentId) {
        this.contentId = contentId;
    }

    public ContentId() {

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
}
