package qna.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteHistories {

	private List<DeleteHistory> deleteHistories = new ArrayList<>();

	public DeleteHistories() {
	}

	public List<DeleteHistory> getDeleteHistories() {
		return deleteHistories;
	}

	public void addDeleteHistory(Question question, User deleter) {
		deleteHistories.add(
			 DeleteHistory.makeDeletedHistory(question, deleter));

		deleteHistories.addAll(question.getAnswers()
			.getAll()
			.stream()
			.map(it -> DeleteHistory.makeDeletedHistory(it, deleter))
			.collect(
				Collectors.toList()));
	}

	public int getNumOfDeletedContents() {
		return deleteHistories.size();
	}
}
