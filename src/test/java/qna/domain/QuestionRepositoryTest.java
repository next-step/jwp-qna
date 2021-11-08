package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("질문 저장소")
class QuestionRepositoryTest {

    private static User javajigi;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("저장")
    void save() {
        //given
        Question question = questionWrittenByJavajigi();

        //when
        Question actual = questionRepository.save(question);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getWriter()).isEqualTo(question.getWriter())
        );
    }

    @Test
    @DisplayName("아이디로 검색")
    void findByIdAndDeletedFalse() {
        //given
        Question question = givenQuestion();

        //when
        Question actual = questionById(question.getId());

        //then
        assertThat(actual)
            .isEqualTo(question);
    }

    @Test
    @DisplayName("삭제되지 않은 질문들 검색")
    void findByQuestionIdAndDeletedFalse() {
        //given
        Question question = givenQuestion();

        // when
        List<Question> actual = questionRepository.findByDeletedFalse();

        //then
        assertThat(actual)
            .contains(question);
    }

    private Question givenQuestion() {
        return questionRepository.save(questionWrittenByJavajigi());
    }

    private Question questionWrittenByJavajigi() {
        return Question.of("title", "contents")
            .writeBy(javajigi);
    }

    private Question questionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }

    @AfterAll
    static void tearDown(@Autowired UserRepository userRepository,
        @Autowired QuestionRepository questionRepository) {
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
