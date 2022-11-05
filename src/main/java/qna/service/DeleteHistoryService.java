package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import qna.domain.DeleteHistories;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;

@Service
public class DeleteHistoryService {
    private DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(DeleteHistories deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories.getDeleteHistories());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(DeleteHistory deleteHistory) {
        deleteHistoryRepository.save(deleteHistory);
    }
}
