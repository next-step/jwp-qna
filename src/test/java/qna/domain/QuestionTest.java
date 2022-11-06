package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import qna.CannotDeleteException;

@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @Test
    void save_question() {
        // given // when
        Question question = questionRepository.save(
            new Question("Test Title", "Test Contesnts").writeBy(UserTest.JAVAJIGI));

        // then
        assertAll(
            () -> assertThat(question.getTitle()).isEqualTo("Test Title"),
            () -> assertThat(question.getWriter()).isEqualTo(UserTest.JAVAJIGI)
        );
    }

    @Test
    void read_question() {
        // given
        Question expectQuestion = questionRepository.save(
            new Question("Test Title", "Test Contesnts").writeBy(UserTest.JAVAJIGI));

        // when
        Optional<Question> question = questionRepository.findById(expectQuestion.getId());

        // then
        assertAll(
            () -> assertThat(question.isPresent()).isTrue(),
            () -> assertThat(expectQuestion).isEqualTo(question.get())
        );
    }

    @Test
    void update_question() {
        // given
        Question question = questionRepository.save(new Question("t1", "con1").writeBy(UserTest.SANJIGI));

        // when
        question.updateWriter(UserTest.JAVAJIGI);
        Question expectQuestion = questionRepository.save(question);

        // then
        assertThat(expectQuestion.getWriter()).isEqualTo(UserTest.JAVAJIGI);
    }

    @Test
    void exception_question_name_null() {
        // given
        Question q1 = new Question(null, null).writeBy(UserTest.SANJIGI);

        // when // then
        assertThatThrownBy(() -> {
            questionRepository.save(q1);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void delete_question() throws Exception {
        // given
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "answer_contents");

        // when
        question.deleteByUser(UserTest.JAVAJIGI, Arrays.asList(answer));
        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        // then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    void delete_question_other_writer() throws Exception {
        // given
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "answer_contents");

        // when, then
        assertThatThrownBy(
            () -> {
                question.deleteByUser(UserTest.SANJIGI, Arrays.asList(answer));
            }
        ).isInstanceOf(CannotDeleteException.class).hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void delete_question_include_answer_by_other() {
        // given
        Question question = new Question("title", "contents").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.SANJIGI, question, "answer_contents");

        // when, then
        assertThatThrownBy(
            () -> {
                question.deleteByUser(UserTest.JAVAJIGI, Arrays.asList(answer));
            }
        ).isInstanceOf(CannotDeleteException.class).hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");

    }
}

