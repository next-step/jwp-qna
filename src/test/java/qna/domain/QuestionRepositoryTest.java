package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("classpath:question.sql")
@Import(TestDataSourceConfig.class)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Question 저장후 동일한지 확인한다.")
    void whenSave_thenSuccess() {
        String title = "뉴스속보!";
        String contents = "개발진행중!";
        Question expected = questionRepository.save(new Question(title, contents));

        assertAll(
                () -> assertThat(expected.getId()).isNotNull(),
                () -> assertThat(expected.getTitle()).isEqualTo(title));

    }

    @Test
    @DisplayName("삭제처리된것을 제외한 질문들을 가져온다.")
    void whenFindByDeletedFalse_thenListSizeTwo() {
        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(2);
    }

    @Test
    @DisplayName("삭제처리된것을 조회할경우 null 을 리턴한다.")
    void givenDeletedTrue_whenFindByIdAndDeletedFalse_thenNull() {
        Question question = questionRepository.findByIdAndDeletedFalse(1L).orElse(null);

        assertThat(question).isNull();
    }

    @Test
    @DisplayName("삭제처리되지않은것을 조회할경우 질문을 리턴한다.")
    void givenDeletedFalse_whenFindByIdAndDeletedFalse_thenSuccess() {
        Long id = questionRepository.findAll().get(2).getId();
        Question question = questionRepository.findByIdAndDeletedFalse(id).orElse(null);

        assertThat(question).isNotNull();
    }
}