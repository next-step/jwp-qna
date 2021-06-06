package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("AnswerRepository 테스트")
class AnswerRepositoryTest {


    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    private Answer answer1;
    private Answer answer2;

    private Answer deletedAnswer1;

    private Question question;

    @BeforeEach
    void setUp() {
        User user = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));

        question = question = questions.save(new Question("title1", "contents1").writeBy(user));

        answer1 = answers.save(new Answer(user, question, "Answers Contents1"));
        answer2 = answers.save(new Answer(user, question, "Answers Contents2"));
        
        Answer deleAnswer1 = new Answer(user, question, "Deleted Content1");
        deleAnswer1.setDeleted(true);
        deletedAnswer1 = answers.save(deleAnswer1);
    }

    @Test
    @DisplayName("findById_정상_저장_전_후_동일한_객체_조회")
    void findById_정상_저장_전_후_동일한_객체_조회() {
        // Given
        Answer expectedResult = answer1;

        // When
        Optional<Answer> actualResult = answers.findById(expectedResult.getId());

        // Then
        assertThat(actualResult).containsSame(expectedResult);
    }

    @Test
    @DisplayName("findByQuestionIdAndDeletedFalse_정상_삭제_되지_않은_데이터만_조회")
    void findByQuestionIdAndDeletedFalse_정상_삭제_되지_않은_데이터만_조회() {
        // Given
        final int expectedResult = 2;

        // When
        List<Answer> foundQuestion = answers.findByQuestionIdAndDeletedFalse(question.getId());

        // Then
        assertThat(foundQuestion.size()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse_정상_데이터_있음")
    void findByIdAndDeletedFalse_정상_데이터_있음() {
        // Given
        Answer nonDeletedAnswer = answer1;

        // When
        Optional<Answer> foundAnswer = answers.findByIdAndDeletedFalse(nonDeletedAnswer.getId());

        // Then
        assertThat(foundAnswer).isPresent();
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse_오류_데이터_없음")
    void findByIdAndDeletedFalse_오류_데이터_없음() {
        // Given
        Answer deletedAnswer = deletedAnswer1;

        // When
        Optional<Answer> foundAnswer = answers.findByIdAndDeletedFalse(deletedAnswer.getId());

        // Then
        assertThat(foundAnswer).isEmpty();
    }
}
