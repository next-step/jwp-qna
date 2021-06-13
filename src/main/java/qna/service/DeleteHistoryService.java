package qna.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.entity.DeleteHistory;
import qna.domain.repository.DeleteHistoryRepository;

@Service
public class DeleteHistoryService {

	private DeleteHistoryRepository deleteHistoryRepository;

	public DeleteHistoryService(DeleteHistoryRepository deleteHistoryRepository) {
		this.deleteHistoryRepository = deleteHistoryRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveAll(List<DeleteHistory> deleteHistories) {
		deleteHistoryRepository.saveAll(deleteHistories);
	}
}
