package qna.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class ContentId {

	private Long contentId;

	protected ContentId() {
	}

	private ContentId(Long contentId) {
		this.contentId = contentId;
	}

	public static ContentId of(Long contentId) {
		return new ContentId(contentId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ContentId contentId = (ContentId)o;

		return Objects.equals(this.contentId, contentId.contentId);
	}

	@Override
	public int hashCode() {
		return contentId != null ? contentId.hashCode() : 0;
	}
}
