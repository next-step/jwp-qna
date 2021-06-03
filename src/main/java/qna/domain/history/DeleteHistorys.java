package qna.domain.history;

import qna.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DeleteHistorys {

	private List<DeleteHistory> deleteHistorys;

	private DeleteHistorys(final List<DeleteHistory> list) {
		this.deleteHistorys = list;
	}

	public static DeleteHistorys of(final DeleteHistory deleteHistory) {
		if (Objects.isNull(deleteHistory)) {
			throw new NotFoundException();
		}
		List<DeleteHistory> list = new ArrayList<>();
		list.add(deleteHistory);
		return of(list);
	}

	public static DeleteHistorys of(final List<DeleteHistory> list) {
		if (Objects.isNull(list)) {
			throw new NotFoundException();
		}
		return new DeleteHistorys(list);
	}

	public void addAll(final DeleteHistorys deleteHistorys) {
		if (Objects.isNull(deleteHistorys)) {
			throw new NotFoundException();
		}
		this.deleteHistorys.addAll(deleteHistorys.deleteHistorys);
	}

	public List<DeleteHistory> values() {
		return Collections.unmodifiableList(this.deleteHistorys);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeleteHistorys that = (DeleteHistorys)o;
		return Objects.equals(deleteHistorys, that.deleteHistorys);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deleteHistorys);
	}
}
