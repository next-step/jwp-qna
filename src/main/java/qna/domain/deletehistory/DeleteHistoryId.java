package qna.domain.deletehistory;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class DeleteHistoryId implements Serializable {

    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    private Long contentId;

    protected DeleteHistoryId() {
    }

    private DeleteHistoryId(ContentType contentType, Long contentId) {
        validate(contentType, contentId);
        this.contentType = contentType;
        this.contentId = contentId;
    }

    private void validate(ContentType contentType, Long contentId) {
        if (contentId == 0) {
            throw new IllegalArgumentException("삭제한 컨텐츠 아이디가 있어야 합니다.");
        }

        if (contentType == null) {
            throw new IllegalArgumentException("컨텐츠 타입이 존재해야 합니다.");
        }
    }

    public static DeleteHistoryId of(ContentType contentType, Long contentId) {
        return new DeleteHistoryId(contentType, contentId);
    }

    public boolean matchContentId(Long id) {
        return this.contentId.compareTo(id) == 0;
    }

    public boolean matchContentType(ContentType contentType) {
        return this.contentType == contentType;
    }

    public boolean isQuestionType() {
        return this.contentType == ContentType.QUESTION;
    }

    public boolean isAnswerType() {
        return this.contentType == ContentType.ANSWER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteHistoryId that = (DeleteHistoryId) o;

        if (contentType != that.contentType) return false;
        return contentId != null ? contentId.equals(that.contentId) : that.contentId == null;
    }

    @Override
    public int hashCode() {
        int result = contentType != null ? contentType.hashCode() : 0;
        result = 31 * result + (contentId != null ? contentId.hashCode() : 0);
        return result;
    }
}
