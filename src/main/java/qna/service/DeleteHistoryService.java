package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.DeleteHistories;

import java.util.List;

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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(DeleteHistory deleteHistory) {
        deleteHistoryRepository.save(deleteHistory);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll_refactor(DeleteHistories deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories.getDeleteHistoryList());
    }
}
