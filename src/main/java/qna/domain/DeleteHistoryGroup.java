package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistoryGroup {

	private List<DeleteHistory> deleteHistories = new ArrayList<>();

	private DeleteHistoryGroup() {
	}

	public static DeleteHistoryGroup generate() {
		return new DeleteHistoryGroup();
	}

	public static DeleteHistoryGroup generateByQuestion(Question question) {
		DeleteHistoryGroup deleteHistoryGroup = generate();
		deleteHistoryGroup.add(question.generateDeleteHistoryOfQuestion());
		deleteHistoryGroup.addAll(question.generateDeleteHistoryAllOfAnswers());
		return deleteHistoryGroup;
	}

	public List<DeleteHistory> deleteHistories() {
		return deleteHistories;
	}

	public void add(DeleteHistory deleteHistory) {
		deleteHistories.add(deleteHistory);
	}

	public void addAll(List<DeleteHistory> deleteHistories) {
		this.deleteHistories.addAll(deleteHistories);
	}
}
