package qna.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void save() {
        Question expected = new Question("JPA 잘쓰는 법", "알려주세요");
        
        Question actual = questionRepository.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @DisplayName("삭제 되지 않은 Question 목록 조회")
    @Test
    void findByDeletedFalse() {
        for (Question question : questionRepository.findByDeletedFalse()) {

            assertThat(question.isDeleted()).isFalse();
        }
    }

    @DisplayName("삭제되지 않은 Quesetion 조회")
    @Test
    void findByIdAndDeletedFalse() {
        Question question = questionRepository.findByIdAndDeletedFalse(3L).get();

        assertAll(
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getTitle()).isEqualTo("오늘 날씨 어떤가요?")
        );
    }
}
