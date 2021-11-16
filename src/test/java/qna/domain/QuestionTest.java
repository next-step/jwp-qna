package qna.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.fixture.AnswerFixture;
import qna.fixture.DeleteHistoryFixture;
import qna.fixture.QuestionFixture;
import qna.fixture.UserFixture;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@DisplayName("Question 테스트")
class QuestionTest {
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
        Question question = QuestionFixture.create("title", "content", user);

        // when
        Question actual = questionRepository.save(question);

        assertThat(actual)
                .isEqualTo(question);
    }

    @DisplayName("findById 확인")
    @Test
    void findById_확인() {
        // given
        User user = userRepository.save(UserFixture.ID가_없는_사용자());
        Question question = QuestionFixture.create("title", "contents", user);

        // when
        Question savedQuestion = questionRepository.save(question);
        Optional<Question> actual = questionRepository.findById(savedQuestion.getId());

        // then
        assertThat(actual)
                .isPresent()
                .contains(savedQuestion);
    }

    @DisplayName("update 확인")
    @Test
    void update_확인() {
        // given
        User user = userRepository.save(UserFixture.ID가_없는_사용자());
        Question question = QuestionFixture.create("title", "contents", user);

        // when
        Question savedQuestion = questionRepository.save(question);
        savedQuestion.setTitle("title2");
        savedQuestion.setContents("contents2");

        Optional<Question> actual = questionRepository.findById(savedQuestion.getId());

        // then
        assertAll(
                () -> assertThat(actual)
                        .isPresent(),
                () -> assertThat(actual.get().getTitle())
                        .isEqualTo("title2"),
                () -> assertThat(actual.get().getContents())
                        .isEqualTo("contents2")
        );
    }

    @DisplayName("delete 테스트")
    @Nested
    class delete {
        @DisplayName("소유자의_question만 존재")
        @Test
        void 소유자의_question만_존재() throws CannotDeleteException {
            // given
            User user = userRepository.save(UserFixture.ID가_없는_사용자());
            Question question = questionRepository.save(QuestionFixture.create("title", "contents", user));

            // when
            List<DeleteHistory> deleteHistories = question.delete(user);

            // then
            assertAll(
                    () -> assertThat(deleteHistories)
                            .hasSize(1)
                            .contains(DeleteHistoryFixture.create(ContentType.QUESTION, question.getId(), question.getWriter())),
                    () -> verifyQuestionDeleteStatus(question.getId(), true)
            );
        }

        @DisplayName("소유자의_question과 Answer만 존재")
        @Test
        void 소유자의_question과_Answer만_존재() throws CannotDeleteException {
            // given
            User user = userRepository.save(UserFixture.ID가_없는_사용자());
            Question question = questionRepository.save(QuestionFixture.create("title", "contents", user));
            Answer answer = answerRepository.save(AnswerFixture.create(user, question, "Answer Contents"));
            question.addAnswer(answer);

            // when
            List<DeleteHistory> deleteHistories = question.delete(user);

            // then
            assertAll(
                    () -> assertThat(deleteHistories)
                            .hasSize(2)
                            .contains(
                                    DeleteHistoryFixture.create(ContentType.QUESTION, question.getId(), question.getWriter()),
                                    DeleteHistoryFixture.create(ContentType.ANSWER, answer.getId(), answer.getWriter())
                            ),
                    () -> verifyDeletedStatus(question.getId(), answer.getId(), true)
            );
        }

        @DisplayName("다른 사용자의 Answer가 존재")
        @Test
        void 다른_사용자의_Answer가_존재() {
            // given
            User user = userRepository.save(UserFixture.ID가_없는_사용자());
            User otherUser = userRepository.save(UserFixture.ID가_없는_다른_사용자());
            Question question = questionRepository.save(QuestionFixture.create("title", "contents", user));
            Answer answer = answerRepository.save(AnswerFixture.create(otherUser, question, "Answer Contents"));
            question.addAnswer(answer);

            // when
            ThrowableAssert.ThrowingCallable throwingCallable = () -> question.delete(otherUser);

            // then
            assertAll(
                    () -> assertThatThrownBy(throwingCallable)
                            .isInstanceOf(CannotDeleteException.class),
                    () -> verifyDeletedStatus(question.getId(), answer.getId(), false)
            );
        }

        private void verifyDeletedStatus(Long questionId, Long answerId, boolean deleteStatus) {
            if (questionId != null) {
                verifyQuestionDeleteStatus(questionId, deleteStatus);
            }

            if (answerId != null) {
                verifyAnswerDeleteStatus(answerId, deleteStatus);
            }
        }

        private void verifyQuestionDeleteStatus(Long questionId, boolean deleteStatus) {
            Optional<Question> actual = questionRepository.findById(questionId);

            assertThat(actual)
                    .isPresent();

            assertThat(actual.get().isDeleted())
                    .isEqualTo(deleteStatus);
        }

        private void verifyAnswerDeleteStatus(Long answerId, boolean deleteStatus) {
            Optional<Answer> actual = answerRepository.findById(answerId);

            assertThat(actual)
                    .isPresent();

            assertThat(actual.get().isDeleted())
                    .isEqualTo(deleteStatus);
        }
    }
}
