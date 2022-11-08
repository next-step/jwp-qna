package qna.domain;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserTest.MINGVEL;

@DisplayName("question 엔티티 테스트")
public class QuestionTest extends TestBase {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question(3L, "title3", "contents3").writeBy(MINGVEL);

    @DisplayName("save 성공")
    @Test
    void save_question_success() {
        assertThat(userRepository.save(MINGVEL).getId()).isNotNull();
    }

    @DisplayName("findByDeletedFalse 메서드 테스트")
    @Test
    void findByDeletedFalse_question_success() {
        //given:
        User user = userRepository.save(MINGVEL);
        Question question = questionRepository.save(provideQuestion(user));
        assertThat(questionRepository.findByDeletedFalse()).containsExactly(question);
    }

    @DisplayName("findByIdAndDeletedFalse 메서드 테스트")
    @Test
    void findByIdAndDeletedFalse_question_success() {
        //given:
        User user = userRepository.save(MINGVEL);
        Question question = questionRepository.save(provideQuestion(user));
        //when:
        Question expected = questionRepository.findByIdAndDeletedFalse(question.getId()).orElse(new Question());
        //then:
        assertThat(expected).isEqualTo(question);
    }

    @DisplayName("title 이 없는 엔티티 저장")
    @Test
    void saveNullTitle_question_DataIntegrityViolationException() {
        User user = userRepository.save(MINGVEL);
        Question question = new Question(null, "contents").writeBy(user);
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 100 이상인 title 을 가지는 엔티티 저장")
    @Test
    void saveOverLengthTitle_question_constraintException() {
        User user = userRepository.save(MINGVEL);
        Question question = new Question(RandomString.make(101), "contents").writeBy(user);
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("save - update 동작 테스트")
    @Test
    void saveUpdate_question_success() {
        //given:
        User user = userRepository.save(MINGVEL);
        Question question = questionRepository.save(provideQuestion(user));
        String modifiedContent = "updated Content";
        //when:
        question.setContents(modifiedContent);
        Question modifiedQuestion = questionRepository.findById(question.getId()).orElse(new Question());
        //then:
        assertThat(modifiedQuestion.getContents()).isEqualTo(modifiedContent);
    }

    @DisplayName("delete 메서드 테스트")
    @Test
    void delete_question_success() {
        //given:
        User user = userRepository.save(MINGVEL);
        Question question = questionRepository.save(provideQuestion(user));
        //when:
        questionRepository.delete(question);
        Question deleted = questionRepository.findById(question.getId()).orElse(null);
        assertThat(deleted).isNull();
    }

    private Question provideQuestion() {
        return provideQuestion(MINGVEL);
    }

    private Question provideQuestion(User user) {
        return new Question(3L, "title3", "contents3").writeBy(user);
    }
}
