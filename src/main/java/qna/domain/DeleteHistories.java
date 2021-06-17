package qna.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class DeleteHistories {
	@OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
	@JoinColumn(name = "delete_history_id")
	private List<DeleteHistory> deleteHistories = new ArrayList<>();

	public List<DeleteHistory> getList() {
		return deleteHistories;
	}

	public void add(DeleteHistory deleteHistory) {
		this.deleteHistories.add(deleteHistory);
	}
}
