package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    private Answer answer1;
    private Answer answer2;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);
        Question question = questionRepository.save(QuestionTest.Q1);
        answer1 = new Answer(javajigi, question, "Answers Contents1");
        answer2 = new Answer(sanjigi, question, "Answers Contents2");
    }

    @DisplayName("Answer 생성")
    @Test
    void teat_save() {
        //given & when
        Answer savedAnswer = answerRepository.save(this.answer1);
        Optional<Answer> findAnswer = answerRepository.findById(savedAnswer.getId());
        //then
        assertAll(
                () -> assertThat(findAnswer.isPresent()).isTrue(),
                () -> assertThat(savedAnswer.equals(findAnswer.get())).isTrue()
        );
    }

    @DisplayName("Question Id로 삭제되지 않은 Answer 목록 조회")
    @Test
    void teat_findByQuestionIdAndDeletedFalse() {
        //given
        this.answer1.setDeleted(true);
        Answer deletedAnswer = answerRepository.save(this.answer1);
        Answer savedAnswer = answerRepository.save(this.answer2);
        //when
        List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(deletedAnswer.getQuestion().getId());
        //then
        assertAll(
                () -> assertThat(findAnswers).hasSize(1),
                () -> assertThat(findAnswers).contains(savedAnswer)
        );
    }

    @DisplayName("Id로 삭제되지 않은 Answer 목록 조회")
    @Test
    void teat_findByIdAndDeletedFalse() {
        //given
        this.answer1.setDeleted(true);
        Answer deletedAnswer = answerRepository.save(answer1);
        Answer savedAnswer = answerRepository.save(answer2);
        //when
        Optional<Answer> findAnswer1 = answerRepository.findByIdAndDeletedFalse(deletedAnswer.getId());
        Optional<Answer> findAnswer2 = answerRepository.findByIdAndDeletedFalse(savedAnswer.getId());
        //then
        assertAll(
                () -> assertThat(findAnswer1.isPresent()).isFalse(),
                () -> assertThat(findAnswer2.isPresent()).isTrue()
        );
    }
}