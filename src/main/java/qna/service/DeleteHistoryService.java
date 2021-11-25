package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.DeleteHistories;
import qna.domain.DeleteHistoryRepository;

@Service
public class DeleteHistoryService {

    private DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional
    public void saveAll(DeleteHistories deleteHistories) {
        deleteHistoryRepository.saveAll(deleteHistories.getValues());
    }
}
