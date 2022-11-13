package qna.domain;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static qna.domain.UserRepositoryTest.MINGVEL;

@DataJpaTest
@DisplayName("question 엔티티 테스트")
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question(1L, new Title("title1"), Contents.from("contents1")).writeBy(
            UserRepositoryTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, new Title("title2"), Contents.from("contents2")).writeBy(
            UserRepositoryTest.SANJIGI);
    public static final Question Q3 = new Question(3L, new Title("title3"), Contents.from("contents3")).writeBy(
            MINGVEL);

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

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
        Question question = new Question(null, Contents.from("contents")).writeBy(user);
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 100 이상인 title 을 가지는 엔티티 저장")
    @Test
    void saveOverLengthTitle_question_constraintException() {
        User user = userRepository.save(MINGVEL);
        Question question = new Question(new Title(RandomString.make(101)), Contents.from("contents")).writeBy(user);
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("save - update 동작 테스트")
    @Test
    void saveUpdate_question_success() {
        //given:
        User user = userRepository.save(MINGVEL);
        Question question = questionRepository.save(provideQuestion(user));
        Contents modifiedContent = Contents.from("updated Content");
        //when:
        question.updateContents(modifiedContent);
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
        return new Question(3L, new Title("title3"), Contents.from("contents3")).writeBy(user);
    }
}
