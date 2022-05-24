package qna.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import qna.domain.Answers;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;
import qna.domain.User;
import qna.exception.NotFoundException;

@Service
public class DeleteHistoryService {
    private QuestionRepository questionRepository;
    private DeleteHistoryRepository deleteHistoryRepository;

    public DeleteHistoryService(QuestionRepository questionRepository,
                                DeleteHistoryRepository deleteHistoryRepository) {
        this.questionRepository = questionRepository;
        this.deleteHistoryRepository = deleteHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<DeleteHistory> findDeleteHistoriesByDeleter(User user) {
        return deleteHistoryRepository.findByDeleter(user);
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
    public void insertDeletedQuestionAndLinkedAnswers(Long questionId) {
        List<DeleteHistory> deleteHistories = new ArrayList<>();
        Optional<Question> optionalQuestion = questionRepository.findByIdAndDeletedTrue(questionId);
        Question question = optionalQuestion.orElseThrow(() -> new NotFoundException("삭제된 질문이 존재하지 않습니다."));
        Answers answers = question.getAnswers();

        deleteHistories.add(DeleteHistory.delete(question));
        deleteHistories.addAll(
                answers.list().stream()
                        .map(DeleteHistory::delete)
                        .collect(Collectors.toList())
        );
        deleteHistoryRepository.saveAll(deleteHistories);
    }
}
