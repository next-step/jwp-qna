package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q3;
import static qna.domain.UserTest.provideUser;

@DisplayName("answer 엔티티 테스트")
class AnswerTest extends TestBase {
    public static final Answer A1 = new Answer(1L, UserTest.JAVAJIGI, Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(2L, UserTest.SANJIGI, QuestionTest.Q2, "Answers Contents2");

    @DisplayName("save 성공")
    @Test
    void save_answer_success() {
        //given:
        assertThatNoException().isThrownBy(() -> answerRepository.save(provideAnswer()));
    }

    @DisplayName("createdAt 자동 생성 여부 테스트")
    @Test
    void save_answer_createdAt() {
        //given, when:
        final Answer answer = answerRepository.save(provideAnswer());
        //then:
        assertThat(answer.getCreatedAt()).isNotNull();
    }

    @DisplayName("findByQuestionIdAndDeletedFalse 메서드 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse_answer_success() {
        //given:
        User user = userRepository.save(UserTest.MINGVEL);
        Question question = questionRepository.save(Q3.writeBy(user));
        Answer answer = answerRepository.save(provideAnswer(user, question));
        //when, then:
        assertThat(answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId())).contains(answer);
    }

    @DisplayName("findByIdAndDeletedFalse 메서드 테스트")
    @Test
    void findByIdAndDeletedFalse_answer_success() {
        //given:
        User user = userRepository.save(UserTest.MINGVEL);
        Question question = questionRepository.save(Q3.writeBy(user));
        Answer answer = answerRepository.save(provideAnswer(user, question));
        //when, then:
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).containsSame(answer);
    }

    @DisplayName("save - Update 동작 테스트")
    @Test
    void saveUpdate_answer_success() {
        //given:
        final User user = userRepository.save(UserTest.MINGVEL);
        final Question question = questionRepository.save(Q3.writeBy(user));
        final Answer answer = answerRepository.save(provideAnswer(user, question));
        final String modifiedContent = "updated Content";
        //when:
        answer.setContents(modifiedContent);
        final Answer modifiedAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).orElse(new Answer());
        //then:
        assertThat(modifiedAnswer.getContents()).isEqualTo(modifiedContent);
    }

    @DisplayName("delete 메서드 테스트")
    @Test
    void delete_answer_success() {
        //given:
        final Answer answer = answerRepository.save(provideAnswer());
        //when:
        answerRepository.delete(answer);
        final Answer resultAnswer = answerRepository.findById(answer.getId()).orElse(null);
        assertThat(resultAnswer).isNull();
    }

    @DisplayName("setWriter 테스트")
    @Test
    void setWriter_answer_success() {
        //given:
        final User user = userRepository.save(provideUser());
        final User changedUser = userRepository.save(UserTest.SANJIGI);
        flushAndClear();
        final Question question = questionRepository.save(Q3.writeBy(user));
        final Answer answer = answerRepository.save(provideAnswer(user, question));
        //when:
        answer.setWriter(changedUser);
        final Answer modifiedAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId()).orElse(new Answer());
        //then:
        assertThat(modifiedAnswer.getWriter()).isEqualTo(changedUser);
    }

    private Answer provideAnswer() {
        return provideAnswer(
                new User("userId", "password", "userName", "email@email.com"),
                new Question("title", "contents"));
    }

    private Answer provideAnswer(User user, Question question) {
        return new Answer(user, question, "content");
    }
}
