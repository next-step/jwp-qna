package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QuestionRepository 테스트")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void question_저장_테스트() {
        Question actual = questionRepository.save(QuestionTest.Q1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertEquals(UserTest.JAVAJIGI.getId(), actual.getWriterId()),
                () -> assertEquals("title1", actual.getTitle()),
                () -> assertEquals("contents1", actual.getContents()),
                () -> assertFalse(actual.isDeleted()),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void question_동일성_보장_테스트() {
        Question questionSaved = questionRepository.save(QuestionTest.Q1);
        Question questionFound = questionRepository.findById(questionSaved.getId()).get();

        assertTrue(questionSaved == questionFound);
    }

    @Test
    void question_업데이트_테스트() {
        Question questionSaved = questionRepository.save(
                new Question("title", "contents")
                        .writeBy(UserTest.JAVAJIGI)
        );
        questionSaved.setContents("질문 변경");

        questionRepository.saveAndFlush(questionSaved);

        assertAll(
                () -> assertEquals("질문 변경", questionSaved.getContents()),
                () -> assertTrue(questionSaved.getUpdatedAt().isAfter(questionSaved.getCreatedAt()))
        );
    }

    @Test
    void delected가_false인_question_목록() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).hasSize(2),
                () -> assertTrue(questions.stream().noneMatch(Question::isDeleted))
        );
    }

    @Test
    void 입력된_id의_delected가_false인_question() {
        Question questionSaved = questionRepository.save(QuestionTest.Q1);
        Question questionFound = questionRepository.findByIdAndDeletedFalse(questionSaved.getId()).get();

        assertFalse(questionFound.isDeleted());
    }
}
