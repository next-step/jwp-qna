package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @AfterEach
    void tearDown() {
        questionRepository.deleteAll();
    }

    @Test
    void save() {
        Question expected = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

        Question actual = questionRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    @DisplayName("JPA가 식별자가 같은 엔티티에 대한 동일성을 보장하는지 테스트")
    void identity() {
        Question expected = saveNewDefaultQuestion();

        Question actual = questionRepository.findById(expected.getId()).get();

        assertThat(actual == expected).isTrue();
    }

    @Test
    @DisplayName("JPA 변경감지로 인한 업데이트 기능 테스트")
    void update() {
        Question expected = saveNewDefaultQuestion();
        expected.setContents("어떻게 살 것인가?");

        Question actual = questionRepository.findById(expected.getId()).get();

        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    @DisplayName("ID로 삭제 후, 조회가 되지 않는지 테스트")
    void delete() {
        Question expected = saveNewDefaultQuestion();
        questionRepository.deleteById(expected.getId());

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> questionRepository.findById(expected.getId()).get()
        );
    }

    @Test
    void findByDeletedFalse() {
        saveNewDefaultQuestion();

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).isNotEmpty();
    }

    @Test
    @DisplayName("저장한 객체에 대해 soft delete를 한 후, findByIdAndDeletedFalse함수로 조회하면 나오지 않는지 테스트")
    void findByIdAndDeletedFalse() {
        Question expected = saveNewDefaultQuestion();
        expected.setDeleted(true);

        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(
            () -> questionRepository.findByIdAndDeletedFalse(expected.getId()).get()
        );
    }

    private Question saveNewDefaultQuestion() {
        Question defaultQuestion = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
        return questionRepository.save(defaultQuestion);
    }
}
