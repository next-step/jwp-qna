package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @DisplayName("DeleteHistory 생성")
    @Test
    void teat_save() {
        //given
        Question question = questionRepository.save(QuestionTest.Q1);
        User user = userRepository.save(UserTest.JAVAJIGI);
        DeleteHistory deleteHistory = new DeleteHistory(ContentType.QUESTION, question.getId(), user.getId(), LocalDateTime.now());
        //when
        DeleteHistory savedDeleteHistory = deleteHistoryRepository.save(deleteHistory);
        Optional<DeleteHistory> findDeleteHistory = deleteHistoryRepository.findById(savedDeleteHistory.getId());
        //then
        assertAll(
                () -> assertThat(findDeleteHistory.isPresent()).isTrue(),
                () -> assertThat(savedDeleteHistory.equals(findDeleteHistory.get())).isTrue()
        );
    }
}