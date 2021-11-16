package qna.domain;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.fixture.AnswerFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("Answer 테스트")
class AnswerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @DisplayName("Save 확인")
    @Test
    void save_확인() {
        // given
        User user = userRepository.save(UserFixture.ID가_없는_사용자());
        Question question = questionRepository.save(QuestionFixture.ID가_없는_사용자의_질문ID가_없는_질문());
        Answer answer = AnswerFixture.create(user, question, "Answers Contents");

        // when
        Answer result = answerRepository.save(answer);

        assertThat(result)
                .isEqualTo(answer);
    }

    @DisplayName("findById 확인")
    @Test
    void findById_확인() {
        // given
        User user = userRepository.save(UserFixture.ID가_없는_사용자());
        Question question = questionRepository.save(QuestionFixture.ID가_없는_사용자의_질문ID가_없는_질문());
        Answer answer = AnswerFixture.create(user, question, "Answers Contents");

        // when
        Answer savedAnswer = answerRepository.save(answer);
        Optional<Answer> actual = answerRepository.findById(savedAnswer.getId());

        // then
        assertThat(actual)
                .isPresent()
                .contains(savedAnswer);
    }

    @DisplayName("update 확인")
    @Test
    void update_확인() {
        // given
        User user = userRepository.save(UserFixture.ID가_없는_사용자());
        Question question = questionRepository.save(QuestionFixture.ID가_없는_사용자의_질문ID가_없는_질문());
        Answer answer = AnswerFixture.create(user, question, "Answers Contents");

        // when
        Answer savedAnswer = answerRepository.save(answer);
        savedAnswer.setContents("Answers Contents2");

        Optional<Answer> actual = answerRepository.findById(savedAnswer.getId());

        // then
        assertAll(
                () -> assertThat(actual)
                        .isPresent(),
                () -> assertThat(actual.get().getContents())
                        .isEqualTo("Answers Contents2")
        );

    }

    @DisplayName("delete 확인")
    @Nested
    class delete {
        @DisplayName("owner가 맞음")
        @Test
        void owner가_맞음() throws CannotDeleteException {
            // given
            User user = userRepository.save(UserFixture.ID가_없는_사용자());
            Question question = questionRepository.save(QuestionFixture.ID가_없는_사용자의_질문ID가_없는_질문());
            Answer answer = answerRepository.save(AnswerFixture.create(user, question, "Answers Contents"));

            // when
            DeleteHistory deleteHistory = answer.delete(user);

            // then
            assertAll(
                    () -> assertThat(answer.isDeleted())
                            .isTrue(),
                    () -> assertThat(deleteHistory)
                            .isNotNull(),
                    () -> assertThat(deleteHistory.getDeletedBy())
                            .isEqualTo(user)
            );
        }

        @DisplayName("owner가 아님")
        @Test
        void owner가_아님() {
            // given
            User user = userRepository.save(UserFixture.ID가_없는_사용자());
            User otherUser = userRepository.save(UserFixture.ID가_없는_다른_사용자());
            Question question = questionRepository.save(QuestionFixture.ID가_없는_사용자의_질문ID가_없는_질문());
            Answer answer = answerRepository.save(AnswerFixture.create(user, question, "Answers Contents"));

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> answer.delete(otherUser);

            // then
            Assertions.assertThatThrownBy(throwingCallable)
                    .isInstanceOf(CannotDeleteException.class);
        }
    }
}
