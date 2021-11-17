package qna.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistories {

	private final List<DeleteHistory> histories;

	private DeleteHistories() {
		this.histories = new ArrayList<>();
	}

	private DeleteHistories(List<DeleteHistory> deleteHistories) {
		this.histories = Collections.unmodifiableList(deleteHistories);
	}

	public static DeleteHistories of(List<DeleteHistory> deleteHistoryList) {
		return new DeleteHistories(deleteHistoryList);
	}

	public static DeleteHistories of(DeleteHistory deleteHistory) {
		return new DeleteHistories(Collections.singletonList(deleteHistory));
	}

	public static DeleteHistories of() {
		return new DeleteHistories();
	}

	public DeleteHistories combine(DeleteHistories other) {
		List<DeleteHistory> deleteHistories = new ArrayList<>();
		deleteHistories.addAll(this.histories);
		deleteHistories.addAll(other.histories);
		return new DeleteHistories(deleteHistories);
	}

	public List<DeleteHistory> toList() {
		return this.histories;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		DeleteHistories that = (DeleteHistories)o;

		return Objects.equals(histories, that.histories);
	}

	@Override
	public int hashCode() {
		return histories != null ? histories.hashCode() : 0;
	}
}
