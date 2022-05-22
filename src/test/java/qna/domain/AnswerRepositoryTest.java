package qna.domain;

import org.junit.jupiter.api.BeforeAll;
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
    private static Answer answer1;
    private static Answer answer2;

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void before() {
        User user1 = new User(1L, "user1", "password", "name", "user1@com");
        User user2 = new User(2L, "user2", "password", "name", "user2@com");

        Question question1 = new Question("title1", "contents1").writeBy(user1);
        Question question2 = new Question("title2", "contents2").writeBy(user2);

        answer1 = new Answer(user1, question1, "Answers Contents1");
        answer2 = new Answer(user2, question2, "Answers Contents1");
    }

    @ParameterizedTest
    @MethodSource("answer와_기댓값을_리턴한다")
    void id를_넘겨주어_deleted가_false인_answer를_찾는다(Answer answer, boolean expected) {
        // given
        answer2.delete();
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
                        answer1,
                        true
                ),
                Arguments.of(
                        answer2,
                        false
                )
        );
    }

    @Test
    void 변경감지() {
        // given
        Answer saved = answerRepository.save(answer1);
        saved.writeContents("update");
        // when
        Optional<Answer> result = answerRepository.findById(saved.getId());
        // then
        assertThat(result)
                .map(Answer::getContents)
                .hasValue("update");
    }
}