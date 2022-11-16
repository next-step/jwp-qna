package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.Q1;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("question_id로 검색 테스트")
    void find_by_question_id() {
        // given
        Question expected = questionRepository.save(Q1);
        // when
        Optional<Question> result = questionRepository.findById(expected.getId());
        // then
        assertThat(result).contains(expected);
    }
}
