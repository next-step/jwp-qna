package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.DeleteHistories;
import qna.repository.DeleteHistoryRepository;

@Service
public class DeleteHistoryService {
    private final DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(DeleteHistories deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories.getDeleteHistories());
    }
}
