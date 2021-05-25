package qna.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("answer 저장 테스트")
    void save() {
        Answer actual = answerRepository.save(AnswerTest.A1);

        Assertions.assertThat(actual).isEqualTo(AnswerTest.A1);
    }

    @Test
    @DisplayName("questionId로 질문에 대한 답변들 정상 리턴하는지 테스트")
    void findByQuestionIdAndDeletedFalse() {

    }

    @Test
    void findByIdAndDeletedFalse() {
    }
}