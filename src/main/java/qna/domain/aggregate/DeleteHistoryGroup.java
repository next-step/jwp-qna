package qna.domain.aggregate;

import java.util.ArrayList;
import java.util.List;
import qna.domain.entity.DeleteHistory;

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

	public void addAll(DeleteHistoryGroup deleteHistoryGroup) {
		this.deleteHistories.addAll(deleteHistoryGroup.deleteHistories);
	}

	public int size() {
		return deleteHistories.size();
	}
}
