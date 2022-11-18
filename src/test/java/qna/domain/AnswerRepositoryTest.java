package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql({"classpath:user.sql", "classpath:question.sql", "classpath:answer.sql"})
@Import(TestDataSourceConfig.class)
@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("특정 질문을 기준으로 답변조회시 n개의 답변 리스트를 리턴한다.")
    void whenFindByQuestionIdAndDeletedFalse_thenSuccess() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(1L);

        assertThat(answers).hasSize(2);
    }

    @Test
    @DisplayName("id 로 삭제되지않은 답변 조회할때 잘못된 id 로 조회할경우 null 리턴한다.")
    void givenInvalidId_whenFindByIdAndDeletedFalse_thenNull() {
        Answer answer = answerRepository.findByIdAndDeletedFalse(432491234L).orElse(null);

        assertThat(answer).isNull();
    }

    @Test
    @DisplayName("id 로 삭제되지않은 답변 조회할때 정상적인 id 로 조회할경우 답변 한개를 리턴한다.")
    void givenValidId_whenFindByIdAndDeletedFalse_thenSuccess() {
        Answer answer = answerRepository.findByIdAndDeletedFalse(1L).orElse(null);

        assertThat(answer).isNotNull();
    }
}