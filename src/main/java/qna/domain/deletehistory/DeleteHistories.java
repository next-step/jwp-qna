package qna.domain.deletehistory;

import java.util.List;

public class DeleteHistories {

	private final List<DeleteHistory> value;

	protected DeleteHistories(List<DeleteHistory> value) {
		this.value = value;
	}

	public boolean add(DeleteHistory deleteHistory){
		return this.value.add(deleteHistory);
	}
}
