package qna.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class DeleteHistoryContentId {
    private Long contentId;

    protected DeleteHistoryContentId() {
    }

    public DeleteHistoryContentId(Long contentId) {
        this.contentId = contentId;
    }

    public Long getContentId() {
        return contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteHistoryContentId that = (DeleteHistoryContentId) o;
        return Objects.equals(contentId, that.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentId);
    }
}
