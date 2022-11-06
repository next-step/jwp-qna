package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

@DataJpaTest
public class AnswerTest {

    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    AnswerRepository answerRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository, @Autowired QuestionRepository questionRepository) {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);
    }

    @Test
    @DisplayName("answer 저장 확인")
    void save_answer() {
        //given, when
        Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Test Content 1"));

        //then
        assertAll(
            () -> assertThat(answer.getWriter()).isEqualTo(UserTest.SANJIGI),
            () -> assertThat(answer.getQuestion()).isEqualTo(QuestionTest.Q1)
        );
    }

    @Test
    @DisplayName("answer 조회 확인")
    void read_answer() {
        //given
        Answer answer = answerRepository.save(new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "Test Content2"));

        //when
        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answer.getId());

        // then
        assertAll(
            () -> assertThat(result.isPresent()).isTrue(),
            () -> assertThat(result.get()).isEqualTo(answer)
        );
    }

    @Test
    @DisplayName("answer 수정 확인")
    void update_answer() {
        //given
        Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Test Content 1"));

        //when
        String updateContents = "update contents";
        answer.updateContents(updateContents);

        Answer result = answerRepository.save(answer);

        // then
        assertAll(
            () -> assertThat(result.getContents()).isEqualTo(updateContents)
        );
    }

    @Test
    @DisplayName("answer 삭제 확인")
    void delete_answer() throws CannotDeleteException {
        // given
        Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Test Content 1"));

        // when
        answer.deleteByUser(UserTest.SANJIGI);
        Optional<Answer> expectAnswer = answerRepository.findByIdAndDeletedFalse(UserTest.SANJIGI.getId());

        // then
        assertThat(expectAnswer.isPresent()).isFalse();
    }

    @Test
    @DisplayName("answer 작성자가 아닌 사용자가 삭제시 예외 발생")
    void delete_repository_other_writer() {
        // given
        Answer answer = answerRepository.save(new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Test Content 1"));

        // when, then
        assertThatThrownBy(
            () -> {
                answer.deleteByUser(UserTest.JAVAJIGI);
            }
        ).isInstanceOf(CannotDeleteException.class).hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }
}
