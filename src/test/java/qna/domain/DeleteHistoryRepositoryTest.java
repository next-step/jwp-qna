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
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    private Question savedQuestion;
    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = new User("applemango", "pw", "name", "contents");
        savedUser = userRepository.save(savedUser);

        savedQuestion = new Question("title2", "contents2").writeBy(savedUser);
        savedQuestion = questionRepository.save(savedQuestion);
    }

    @DisplayName("삭제이력 저장 시, id가 생성되는지 확인한다")
    @Test
    void check_save() {
        //When
        DeleteHistory deleteHistory
                = DeleteHistory.ofQuestion(savedQuestion.getId(), savedUser);
        deleteHistory = deleteHistoryRepository.save(deleteHistory);

        //Then
        assertThat(deleteHistory.getId()).isNotNull();
    }
}
