package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;

@Service
public class DeleteHistoryService {
    private final DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(final DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(final Iterable<DeleteHistory> entities) {
        deleteHistoryRepository.saveAll(entities);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(final DeleteHistory deleteHistory) {
        deleteHistoryRepository.save(deleteHistory);
    }
}
