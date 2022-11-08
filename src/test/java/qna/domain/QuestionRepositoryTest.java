package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.answer.Answer;
import qna.domain.answer.Answers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        questionRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
    }

    @Test
    @DisplayName("질문을 저장할 수 있어야 한다.")
    void save_question() {
        Question question = Q1;
        Question savedQuestion = questionRepository.save(question);
        assertThat(savedQuestion.getId()).isNotNull();

        assertAll(
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(savedQuestion.getCreatedAt()).isNotNull());
    }

    @Test
    @DisplayName("QuestionId로 삭제 상태가 아닌 Question 가져올 수 있어야 한다.")
    void get_question_by_id() {
        questionRepository.save(Q1);

        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(Q1.getId());

        assertThat(question).contains(Q1);
    }
    @Test
    @DisplayName("삭제된 Question 조회되지 않는다.")
    void do_not_get_deleted_question() {
        List<Question> savedQuestions = questionRepository.saveAll(Arrays.asList(Q1, Q2));
        questionRepository.deleteById(savedQuestions.get(0).getId());

        List<Question> restQuestions = questionRepository.findByDeletedFalse();

        assertThat(savedQuestions.size()-1).isEqualTo(restQuestions.size());
    }

    @Test
    @DisplayName("Question 내용 변경")
    void change_question_writer() {
        List<Question> savedQuestions = questionRepository.saveAll(Arrays.asList(Q1, Q2));

        Question question1 = savedQuestions.get(0);
        question1.updateContents("다른 내용");

        Question updatedQuestion = questionRepository.findByIdAndDeletedFalse(question1.getId()).get();

        assertThat(question1.getContents()).isEqualTo(updatedQuestion.getContents());
    }

    @Test
    @DisplayName("Question 작성자만 삭제할 수 있다.")
    void question_deleted_validate_writer() throws CannotDeleteException {
        List<Question> savedQuestions = questionRepository.saveAll(Arrays.asList(Q1, Q2));
        Question question = savedQuestions.get(0);

        assertThatThrownBy(() -> question.checkWriter(SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
        question.checkWriter(JAVAJIGI);
    }

    @Test
    @DisplayName("Question은 다른 사람의 답변이 없는 경우만 삭제 가능하다.")
    void question_deleted_validate_answers() throws CannotDeleteException {
        List<Question> savedQuestions = questionRepository.saveAll(Arrays.asList(Q1, Q2));
        Question question1 = savedQuestions.get(0);
        question1.addAnswer(new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1"));

        Question question2 = savedQuestions.get(1);
        question2.addAnswer(new Answer(UserTest.JAVAJIGI, QuestionTest.Q2, "Second Answers Contents1"));
        question2.addAnswer(new Answer(SANJIGI, QuestionTest.Q2, "Second Answers Contents1"));

        Answers answers1 = new Answers(question1.getAnswers());
        answers1.validateDeleteAnswer(JAVAJIGI);

        Answers answers2 = new Answers(question2.getAnswers());
        assertThatThrownBy(() -> answers2.validateDeleteAnswer(SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
