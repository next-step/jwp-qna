package qna.domain;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DeleteHistories {
	private List<DeleteHistory> deleteHistories;

	private DeleteHistories(List<DeleteHistory> deleteHistories) {
		this.deleteHistories = deleteHistories;
	}

	public static DeleteHistories of(List<DeleteHistory> deleteHistories) {
		return new DeleteHistories(deleteHistories);
	}

	static DeleteHistories of(DeleteHistory... deleteHistories) {
		return new DeleteHistories(asList(deleteHistories));
	}

	public List<DeleteHistory> getDeleteHistories() {
		return Collections.unmodifiableList(deleteHistories);
	}

	DeleteHistories concat(DeleteHistories otherDeleteHistories) {
		List<DeleteHistory> concatDeleteHistories = Stream.concat(
			this.deleteHistories.stream(),
			otherDeleteHistories.deleteHistories.stream()
		).collect(toList());

		return new DeleteHistories(concatDeleteHistories);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DeleteHistories that = (DeleteHistories)o;
		return Objects.equals(deleteHistories, that.deleteHistories);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleteHistories);
	}
}
