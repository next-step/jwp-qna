package qna.domain;

import java.util.Collections;
import java.util.List;

public class DeleteHistories {
	private final List<DeleteHistory> values;

	private DeleteHistories(List<DeleteHistory> values) {
		this.values = Collections.unmodifiableList(values);
	}

	public static DeleteHistories of(List<DeleteHistory> deleteHistories) {
		return new DeleteHistories(deleteHistories);
	}

	public List<DeleteHistory> getValues() {
		return values;
	}
}
