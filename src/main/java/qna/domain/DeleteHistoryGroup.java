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

	public List<DeleteHistory> deleteHistories() {
		return deleteHistories;
	}

	public void add(DeleteHistory deleteHistory) {
		deleteHistories.add(deleteHistory);
	}

	public void addAll(List<DeleteHistory> deleteHistories) {
		this.deleteHistories.addAll(deleteHistories);
	}

	public int size() {
		return deleteHistories.size();
	}
}
