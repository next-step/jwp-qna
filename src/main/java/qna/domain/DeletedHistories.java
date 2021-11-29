package qna.domain;

import java.util.ArrayList;
import java.util.List;

public class DeletedHistories {

	private List<DeleteHistory> deleteHistories;

	public DeletedHistories() {
		this.deleteHistories = new ArrayList<DeleteHistory>();
	}

	public DeletedHistories addDeleteHistory(ContentType contentType, Long id, User writer) {
		DeleteHistory deleteHistory = new DeleteHistory(contentType, id, writer);
		addDeleteHistory(deleteHistory);
		return this;
	}

	public DeletedHistories addDeleteHistory(DeleteHistory deleteHistory) {
		this.deleteHistories.add(deleteHistory);
		return this;
	}

	public List<DeleteHistory> getDeleteHistories() {
		return this.deleteHistories;
	}
}
