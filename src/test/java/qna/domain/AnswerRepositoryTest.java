package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("save하면 id가 자동으로 생성되야 한다.")
    void saveTest() {
        Answer answer = answerRepository.save(AnswerTest.A1);

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents())
        );
    }

    @Test
    @DisplayName("findById 결과는 동일성이 보장되어야한다.")
    void findByIdTest() {
        Answer save = answerRepository.save(AnswerTest.A1);
        Answer find = answerRepository.findById(save.getId()).get();

        assertAll(
                () -> assertThat(find).isEqualTo(save),
                () -> assertThat(find.getId()).isEqualTo(save.getId())
        );
    }

    @Test
    @DisplayName("nullable false 칼럼은 반드시 값이 있어야한다.")
    void nullableTest() {
        Answer save = answerRepository.save(AnswerTest.A1);
        Answer find = answerRepository.findById(save.getId()).get();

        assertAll(
                () -> assertThat(find.getCreatedAt()).isBefore(LocalDateTime.now()),
                () -> assertThat(find.isDeleted()).isFalse()
        );
    }
}