package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeleteHistories {

	private List<DeleteHistory> deleteHistoryList = new ArrayList<>();

	public List<DeleteHistory> getDeleteHistoryList() {
		return deleteHistoryList;
	}

	public void add(DeleteHistory deleteHistory) {
		deleteHistoryList.add(deleteHistory);
	}
}