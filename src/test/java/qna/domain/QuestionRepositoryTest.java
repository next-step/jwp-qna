package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("QuestionRepository 테스트")
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    private User javajigi;
    private User sanjigi;

    private Question question1;
    private Question question2;

    private Question deletedQuestion1;
    private Question deletedQuestion2;

    @BeforeEach
    void setUp() throws CannotDeleteException {
        javajigi = users.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        sanjigi = users.save(new User("sanjigi", "password", "name", "sanjigi@slipp.net"));

        question1 = questions.save(new Question(new Title("title1"), new Contents("contents1")).writeBy(javajigi));
        question2 = questions.save(new Question(new Title("title2"), new Contents("contents2")).writeBy(sanjigi));

        deletedQuestion1 = new Question(new Title("deleted question title1"), new Contents("deleted question content1")).writeBy(sanjigi);
        deletedQuestion1.delete(sanjigi);
        deletedQuestion1 = questions.save(deletedQuestion1);

        deletedQuestion2 = new Question(new Title("deleted question title2"), new Contents("deleted question content2")).writeBy(sanjigi);
        deletedQuestion2.delete(sanjigi);
        deletedQuestion2 = questions.save(deletedQuestion2);
    }

    @Test
    @DisplayName("findById_정상_저장_전_후_동일한_객체_조회")
    void findById_정상_저장_전_후_동일한_객체_조회() {
        // Given
        Question expectedResult = question1;

        // When
        Optional<Question> actualResult = questions.findById(expectedResult.getId());

        // Then
        assertThat(actualResult).containsSame(expectedResult);
    }

    @Test
    @DisplayName("findByDeletedFalse_정상_삭제_상태가_아닌_모든_Question_조회")
    void findByDeletedFalse_정상_삭제_상태가_아닌_모든_Question_조회() {
        // Given
        int expectedResult = 2;

        // When
        List<Question> actualResult = questions.findByDeletedFalse();

        // Then
        assertThat(actualResult.size()).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse_정상_결과있음")
    void findByIdAndDeletedFalse_정상_결과있음() {
        // Given
        long targetId = question1.getId();

        // When
        Question actualResult = questions.findByIdAndDeletedFalse(targetId)
                .orElseThrow(EntityNotFoundException::new);

        // Then
        assertThat(actualResult).isEqualTo(question1);
    }

    @Test
    @DisplayName("findByIdAndDeletedFalse_정상_결과없음")
    void findByIdAndDeletedFalse_정상_결과없음() {
        // Given
        long targetId = deletedQuestion1.getId();

        // When
        Optional<Question> actualResult = questions.findByIdAndDeletedFalse(targetId);

        // Then
        assertThat(actualResult).isEmpty();
    }

}