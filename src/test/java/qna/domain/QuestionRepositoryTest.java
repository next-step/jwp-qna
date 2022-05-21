package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("삭제되지 않은 Question 조회")
    @Test
    void findByDeletedFalse() {
        //given
        QuestionTest.Q1.setDeleted(true);
        questionRepository.save(QuestionTest.Q1);
        final Question expected = questionRepository.save(QuestionTest.Q2);

        //when
        final List<Question> actual = questionRepository.findByDeletedFalse();

        //then
        assertThat(actual).hasSize(1);
        assertThat(actual).contains(expected);
    }

    @DisplayName("id로 삭제되지 않은 Question 조회")
    @Test
    void findByIdAndDeletedFalse() {
        //given
        final Question expected = questionRepository.save(QuestionTest.Q1);

        //when
        final Question actual = questionRepository.findByIdAndDeletedFalse(expected.getId()).get();

        //then
        assertThat(actual).isEqualTo(expected);
    }
}