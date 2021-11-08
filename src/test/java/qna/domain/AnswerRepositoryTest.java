package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("답변 저장소")
class AnswerRepositoryTest {

    private static User javajigi;
    private static Question questionWrittenByJavajigi;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository,
        @Autowired QuestionRepository questionRepository) {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        questionWrittenByJavajigi = questionRepository.save(
            Question.of("title", "contents").writeBy(javajigi));
    }

    @Test
    @DisplayName("저장")
    void save() {
        //given
        Answer answer = Answer.of(javajigi, questionWrittenByJavajigi, "contents");

        //when
        Answer actual = answerRepository.save(answer);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(actual.getQuestion()).isEqualTo(answer.getQuestion()),
            () -> assertThat(actual.getWriter()).isEqualTo(answer.getWriter())
        );
    }

    @Test
    @DisplayName("아이디로 검색")
    void findByIdAndDeletedFalse() {
        //given
        Answer answer = givenAnswer();

        //when
        Answer actual = answerById(answer.getId());

        //then
        assertThat(actual)
            .isEqualTo(answer);
    }

    private Answer givenAnswer() {
        return answerRepository.save(Answer.of(javajigi, questionWrittenByJavajigi, "contents"));
    }

    private Answer answerById(Long id) {
        return answerRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(
                () -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }

    @AfterAll
    static void tearDown(@Autowired UserRepository userRepository,
        @Autowired QuestionRepository questionRepository,
        @Autowired AnswerRepository answerRepository) {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }
}
