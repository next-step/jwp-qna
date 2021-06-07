package qna.domain;

import org.springframework.transaction.annotation.Transactional;
import qna.service.DeleteHistoryService;

import java.util.List;

public class DeleteHistories {
	private final List<DeleteHistory> deleteHistories;

	public DeleteHistories(List<DeleteHistory> deleteHistories) {
		this.deleteHistories = deleteHistories;
	}

	public List<DeleteHistory> getDeleteHistories() {
		return deleteHistories;
	}

	@Transactional
	public DeleteHistories save(DeleteHistoryService deleteHistoryService) {
		return new DeleteHistories(deleteHistoryService.saveAll(deleteHistories));
	}
}
