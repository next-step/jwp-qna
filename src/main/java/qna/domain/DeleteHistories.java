package qna.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {

	private final List<DeleteHistory> deleteHistoryList;

	private DeleteHistories(List<DeleteHistory> deleteHistoryList) {
		this.deleteHistoryList = deleteHistoryList;
	}

	public static DeleteHistories of(DeleteHistory questionDeleteHistory, List<DeleteHistory> answerDeleteHistory) {
		return new DeleteHistories(
			Stream.concat(
				Stream.of(questionDeleteHistory), answerDeleteHistory.stream())
				.collect(Collectors.toList()));
	}

	public List<DeleteHistory> getDeleteHistories() {
		return deleteHistoryList;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeleteHistories that = (DeleteHistories)o;
		return deleteHistoryList.equals(that.deleteHistoryList);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleteHistoryList);
	}

	@Override
	public String toString() {
		return "DeleteHistories{" +
			"deleteHistoryList=" + deleteHistoryList +
			'}';
	}
}
