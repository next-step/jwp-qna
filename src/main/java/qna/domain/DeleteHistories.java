package qna.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DeleteHistories {

	private final List<DeleteHistory> deleteHistoryList;

	public DeleteHistories(List<DeleteHistory> deleteHistoryList) {
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
}
