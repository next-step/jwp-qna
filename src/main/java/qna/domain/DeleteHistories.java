package qna.domain;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {
	private final List<DeleteHistory> deleteHistories;

	private DeleteHistories(List<DeleteHistory> deleteHistories) {
		this.deleteHistories = deleteHistories;
	}

	public static DeleteHistories of(List<DeleteHistory> deleteHistories)  {
		return new DeleteHistories(deleteHistories);
	}

	public List<DeleteHistory> getValues() {
		return Collections.unmodifiableList(deleteHistories);
	}
}
