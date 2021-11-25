package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;

import java.util.List;

@Service
public class DeleteHistoryService {
    private final DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    public void saveAll(List<DeleteHistory> deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories);
    }

    public void save(DeleteHistory deleteHistory) {
        deleteHistoryRepository.save(deleteHistory);
    }
}
