package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void initialize() {
        user = userRepository.save(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("Question 저장")
    void save(){
        Question saved = questionRepository.save(new Question("title1", "contents1").writeBy(user));
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("Question 조회: by DeletedFalse")
    void Question_조회_by_DeletedFalse(){
        questionRepository.save(QuestionTest.generateQuestion(user, true));
        Question questionDeletedFalse = questionRepository.save(QuestionTest.generateQuestion(user, false));
        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).containsExactly(questionDeletedFalse);
    }

    @ParameterizedTest(name = "삭제되지 않은 Question 조회: deleted = {0}, 조회값 존재 = {1}")
    @DisplayName("Question 조회: by Id, DeletedFalse")
    @MethodSource("provideBooleansForQuestionFind")
    void Question_조회_byId_DeletedFalse(boolean deleted, boolean resultPresent){
        Question question = questionRepository.save(QuestionTest.generateQuestion(user, deleted));
        assertThat(questionRepository.findByIdAndDeletedFalse(question.getId()).isPresent()).isEqualTo(resultPresent);
    }

    private static Stream<Arguments> provideBooleansForQuestionFind() {
        return Stream.of(
                Arguments.of(true, false),
                Arguments.of(false, true)
        );
    }
}
