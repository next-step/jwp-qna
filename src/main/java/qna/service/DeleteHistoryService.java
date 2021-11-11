package qna.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteHistoryService {

    private final DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(DeleteHistoryRepository deleteHistoryRepository) {
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAll(Question question) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        deleteHistories.add(DeleteHistory.createBy(question));

        for (Answer answer : question.getAnswers()) {
            deleteHistories.add(DeleteHistory.createBy(answer));
        }

        deleteHistoryRepository.saveAll(deleteHistories);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(DeleteHistory deleteHistory) {
        deleteHistoryRepository.save(deleteHistory);
    }
}
