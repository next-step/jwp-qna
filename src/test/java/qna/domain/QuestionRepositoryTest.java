package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class QuestionRepositoryTest {
    private Question question1;
    private Question question2;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);
        this.question1 = new Question("title1", "contents1").writeBy(javajigi);
        this.question2 = new Question("title2", "contents2").writeBy(sanjigi);
    }

    @DisplayName("Question 생성")
    @Test
    void teat_save() {
        //given & when
        Question savedQuestion = questionRepository.save(this.question1);
        Optional<Question> findQuestion = questionRepository.findById(savedQuestion.getId());
        //then
        assertAll(
                () -> assertThat(findQuestion.isPresent()).isTrue(),
                () -> assertThat(savedQuestion.equals(findQuestion.get())).isTrue()
        );
    }

    @DisplayName("삭제되지 않은 Question 목록 조회")
    @Test
    void teat_findByDeletedFalse() throws CannotDeleteException {
        //given
        this.question1.delete(UserTest.JAVAJIGI);
        questionRepository.save(this.question1);
        Question savedQuestion = questionRepository.save(this.question2);
        //when
        List<Question> findQuestions = questionRepository.findByDeletedFalse();
        //then
        assertAll(
                () -> assertThat(findQuestions).hasSize(1),
                () -> assertThat(findQuestions).contains(savedQuestion)
        );
    }

    @DisplayName("Id로 삭제되지 않은 Question 목록 조회")
    @Test
    void teat_findByIdAndDeletedFalse() throws CannotDeleteException {
        //given
        this.question1.delete(UserTest.JAVAJIGI);
        Question deletedQuestion = questionRepository.save(this.question1);
        Question savedQuestion = questionRepository.save(this.question2);
        //when
        Optional<Question> findDeletedQuestions = questionRepository.findByIdAndDeletedFalse(deletedQuestion.getId());
        Optional<Question> findSavedQuestions = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());
        //then
        assertAll(
                () -> assertThat(findDeletedQuestions.isPresent()).isFalse(),
                () -> assertThat(findSavedQuestions.isPresent()).isTrue()
        );
    }
}