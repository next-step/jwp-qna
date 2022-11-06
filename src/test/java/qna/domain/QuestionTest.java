package qna.domain;

import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("question 엔티티 테스트")
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question(1L, "title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question(2L, "title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question(3L, "title3", "contents3").writeBy(UserTest.MINGVEL);
    public static final Question DELETE_SOON_QUESTION = new Question(4L, "title4", "contents4").writeBy(UserTest.MINGVEL);

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("save 성공")
    @Test
    void save_question_success() {
        assertThatNoException().isThrownBy(() -> questionRepository.save(Q1));
    }

    @DisplayName("findByDeletedFalse 메서드 테스트")
    @Test
    void findByDeletedFalse_question_success() {
        //given:
        Question question = questionRepository.save(Q1);
        assertThat(questionRepository.findByDeletedFalse()).containsExactly(question);
    }

    @DisplayName("findByIdAndDeletedFalse 메서드 테스트")
    @Test
    void findByIdAndDeletedFalse_question_success() {
        //given:
        Question question = questionRepository.save(Q3);
        //when:
        Question expected = questionRepository.findByIdAndDeletedFalse(question.getId()).orElse(new Question());
        //then:
        assertThat(expected).isEqualTo(question);
    }

    @DisplayName("title 이 없는 엔티티 저장")
    @Test
    void saveNullTitle_question_DataIntegrityViolationException() {
        Question question = new Question(null, "contents");
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("길이가 100 이상인 title 을 가지는 엔티티 저장")
    @Test
    void saveOverLengthTitle_question_constraintException() {
        Question question = new Question(RandomString.make(101), "contents");
        assertThatThrownBy(() -> questionRepository.save(question))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("save - update 동작 테스트")
    @Test
    void saveUpdate_question_success() {
        //given:
        Question question = questionRepository.save(Q2);
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
        Question question = questionRepository.save(DELETE_SOON_QUESTION);
        //when:
        questionRepository.delete(question);
        Question deleted = questionRepository.findById(question.getId()).orElse(null);
        assertThat(deleted).isNull();
    }
}
