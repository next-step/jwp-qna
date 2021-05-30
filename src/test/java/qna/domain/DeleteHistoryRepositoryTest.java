package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    DeleteHistoryRepository deleteHistoryRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    private Question question;

    @BeforeEach
    void setUp() {
        question = questionRepository.save(QuestionTest.Q1);
        answerRepository.save(AnswerTest.A1);
    }

    @DisplayName("삭제이력 저장 시, id가 생성되는지 확인한다")
    @Test
    void check_save() {
        //When
        DeleteHistory deleteHistory = deleteHistoryRepository.save(
                new DeleteHistory(ContentType.QUESTION, question.getId(),
                        UserTest.JAVAJIGI.getId(), LocalDateTime.now())
        );

        //Then
        assertThat(deleteHistory.getId()).isNotNull();
    }
}
