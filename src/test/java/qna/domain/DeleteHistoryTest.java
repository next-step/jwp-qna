package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.collections.DeleteHistories;
import qna.domain.repository.QuestionRepository;
import qna.exception.CannotDeleteException;

@DataJpaTest
class DeleteHistoryTest {

    @Autowired
    private QuestionRepository questionRepository;


    @DisplayName("질문 삭제(답변 포함)에 대한 히스토리 리스트를 생성한다.")
    @Test
    void createDeleteHistories() throws CannotDeleteException {
        long questionId = 4L;
        Question question = questionRepository.findById(questionId).get();
        User writer = question.getWriter();
        question.delete(writer);

        DeleteHistories deleteHistories = DeleteHistory.createDeleteHistories(question);
        List<DeleteHistory> actual = deleteHistories.getDeleteHistories();
        assertThat(actual).hasSize(3);
    }

}
