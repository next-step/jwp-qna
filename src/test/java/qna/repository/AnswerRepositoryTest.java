package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    private User writer;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAllInBatch();
        answerRepository.deleteAllInBatch();
        writer = User.create("gerrad", "password", "humba", "gerrad@liverpool.uk");
        question = Question.create("title", "contents", writer);
        answer = Answer.create(writer, question, "contents");
    }

    @Test
    @DisplayName("answer 저장 테스트")
    void save() {
        // when
        Answer saverAnswer = answerRepository.save(answer);
        // then
        assertAll(
                () -> assertThat(saverAnswer.getId()).isNotNull(),
                () -> assertThat(saverAnswer.getContents()).isEqualTo(answer.getContents())
        );
    }

    @Test
    @DisplayName("answer 저장 후 조회 테스트")
    void findByIdAndDeletedFalse_test() {
        User writer2 = User.create("tiago", "password", "alkantra", "tiago@liverpool.uk");
        Question question2 = Question.create("title", "contents", writer2);
        Answer answer2 = Answer.create(writer2, question2, "contents");
        // when
        answerRepository.save(answer);
        answerRepository.save(answer2);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());
        // then
        assertThat(findAnswer).isPresent();
        findAnswer.ifPresent(paramAnswer -> assertAll(
                () -> assertThat(paramAnswer).isEqualTo(answer),
                () -> assertThat(paramAnswer.isDeleted()).isFalse(),
                () -> assertThat(paramAnswer).isNotEqualTo(answer2)
        ));
    }

    @Test
    @DisplayName("answer 삭제 set 후 조회 시 미조회 테스트")
    void set_delete_find_test() {
        // given
        Long id = answer.getId();
        // when
        answer.remove(writer);
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(id);
        // then
        assertThat(findAnswer).isNotPresent();
    }

    @Test
    @DisplayName("answer 삭제 후 조회 시 미조회 테스트")
    void delete_find_test() {
        // given
        Long id = answer.getId();
        Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(id);
        // when
        findAnswer.ifPresent(paramAnswer -> answerRepository.delete(paramAnswer));
        Optional<Answer> deletedAnswer = answerRepository.findByIdAndDeletedFalse(id);
        // then
        assertThat(deletedAnswer).isNotPresent();
    }

    @Test
    void cascade_remove_test() {
        // given
        Answer saveAnswer = answerRepository.save(answer);
        Question saveQuestion = questionRepository.save(question);

        // when
        answerRepository.delete(saveAnswer);
        Optional<Question> findQuestion = questionRepository.findById(saveQuestion.getId());
        // then
        assertThat(findQuestion).isPresent();
    }
}
