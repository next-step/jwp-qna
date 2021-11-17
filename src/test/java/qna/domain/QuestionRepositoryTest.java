package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("save하면 id가 자동으로 생성되야 한다.")
    void saveTest() {
        assertThat(QuestionTest.Q1.getId()).isNull();
        Question save = questionRepository.save(QuestionTest.Q1);

        assertAll(
                () -> assertThat(save.getId()).isNotNull(),
                () -> assertThat(QuestionTest.Q1.getId()).isNotNull(),
                () -> assertThat(save.getWriterId()).isEqualTo(QuestionTest.Q1.getWriterId())
        );
    }
    
    @Test
    @DisplayName("findById 결과는 동일성이 보장되어야한다.")
    void findByIdTest() {
        Question save = questionRepository.save(QuestionTest.Q1);

        Question find = questionRepository.findById(save.getId()).get();

        assertThat(find).isEqualTo(save);
    }

    @Test
    @DisplayName("nullable false 칼럼은 반드시 값이 있어야한다.")
    void nullableTest() {
        Question save = questionRepository.save(QuestionTest.Q1);
        Question find = questionRepository.findById(save.getId()).get();

        assertAll(
                () -> assertThat(find.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(find.isDeleted()).isFalse(),
                () -> assertThat(find.getTitle()).isNotNull()
        );
    }
}