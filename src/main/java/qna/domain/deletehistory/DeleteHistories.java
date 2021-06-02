package qna.domain.deletehistory;

import static java.util.Objects.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

	private final List<DeleteHistory> value;

	public DeleteHistories(List<DeleteHistory> value) {
		requireNonNull(value, "Found Null Value in DeleteHistories");
		this.value = value;
	}

	public void add(DeleteHistory deleteHistory){
		this.value.add(deleteHistory);
	}

	public List<DeleteHistory> getValue() {
		return Collections.unmodifiableList(value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof DeleteHistories))
			return false;
		DeleteHistories that = (DeleteHistories)o;
		return Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
