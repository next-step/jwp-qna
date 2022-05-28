package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import qna.annotation.QnaDataJpaTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@QnaDataJpaTest
class AnswerRepositoryTest {
    private Answer answer;

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void before() {
        User user = new User("user1", "password", "name", "user1@com");
        Question question = new Question("title1", "contents1");

        answer = new Answer(user, question, "Answers Contents1");
    }

    @ParameterizedTest
    @MethodSource("answer와_기댓값을_리턴한다")
    void id를_넘겨주어_deleted가_false인_answer를_찾는다(Answer answer, boolean expected) {
        // given
        answer.delete(answer.getWriter());
        userRepository.save(answer.getWriter());
        questionRepository.save(answer.getQuestion());
        Answer saved = answerRepository.save(answer);
        // when
        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(saved.getId());
        // then
        assertThat(result.isPresent()).isEqualTo(expected);
    }

    static Stream<Arguments> answer와_기댓값을_리턴한다() {
        return Stream.of(
                Arguments.of(
                        getAnswer(new User("user1", "password", "name", "user1@com"),
                                  new Question("title1", "contents1")),
                        false
                ),
                Arguments.of(
                        getAnswer(new User( "user2", "password", "name", "user2@com"),
                                  new Question("title2", "contents2")),
                        false
                )
        );
    }

    private static Answer getAnswer(User user, Question question) {
        return new Answer(user, question, "Answers Contents");
    }

    @Test
    void 변경감지() {
        // given
        userRepository.save(answer.getWriter());
        questionRepository.save(answer.getQuestion());
        Answer saved = answerRepository.save(answer);

        saved.writeContents("update");
        // when
        Optional<Answer> result = answerRepository.findById(saved.getId());
        // then
        assertThat(result)
                .map(Answer::getContents)
                .hasValue("update");
    }
}