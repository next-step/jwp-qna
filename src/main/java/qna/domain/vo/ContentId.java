package qna.domain.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ContentId {

	@Column(name = "content_id")
	private Long contentId;

	protected ContentId() {
	}

	private ContentId(Long contentId) {
		this.contentId = contentId;
	}

	public static ContentId generate(Long contentId) {
		validateContentIdIsNotNull(contentId);
		return new ContentId(contentId);
	}

	private static void validateContentIdIsNotNull(Long contentId) {
		if (Objects.isNull(contentId)) {
			throw new IllegalArgumentException("반드시 답변 혹은 질문의 아이디가 입력되어야 합니다.");
		}
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof ContentId)) {
			return false;
		}
		ContentId contentId1 = (ContentId) object;
		return Objects.equals(contentId, contentId1.contentId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(contentId);
	}
}
