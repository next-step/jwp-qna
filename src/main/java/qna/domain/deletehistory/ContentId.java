package qna.domain.deletehistory;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ContentId {

	@Column(name = "contentId")
	private Long value;

	public ContentId(Long value) {
		this.value = value;
	}

	public ContentId() { }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ContentId))
			return false;
		ContentId contentId = (ContentId)o;
		return Objects.equals(value, contentId.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
