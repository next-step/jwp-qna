package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;
import qna.domain.UserTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Answer answer;
    private Question question;
    private User writer;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title", "contents").writeBy(writer));
        answer = answerRepository.save(new Answer(writer, question, "answer contents"));
    }

    @Test
    @DisplayName("Question 연관관계 테스트")
    void Answer_to_Question() {
        em.clear();
        Optional<Answer> optionalAnswer = answerRepository.findById(this.answer.getId());
        assertThat(optionalAnswer.get().getQuestion().getId()).isEqualTo(question.getId());
    }

    @Test
    @DisplayName("User(writer) 연관관계 테스트")
    void Answer_to_Writer() {
        em.clear();
        Optional<Answer> optionalAnswer = answerRepository.findById(this.answer.getId());
        assertThat(optionalAnswer.get().getWriter().getId()).isEqualTo(writer.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 상태의 answer를 id 기준으로 검색")
    void Answer_findByIdAndDeletedFalse() {
        em.clear();
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(optionalAnswer.isPresent()).isTrue();

        optionalAnswer.get().setDeleted(true);
        answerRepository.flush();
        em.clear();

        optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(optionalAnswer.isPresent()).isFalse();
    }

    @Test
    @DisplayName("삭제되지 않았은 상태의 answer를 question_id 기준으로 검색")
    void Answer_findByQuestionIdAndDeletedFalse() {
        Answer answer = answerRepository.save(new Answer(writer, question, "answer delete contents"));
        answer.setDeleted(true);
        answerRepository.flush();
        em.clear();

        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());

        assertAll(
            () -> assertThat(answerList).hasSize(1),
            () -> assertThat(answerList.get(0).getId()).isEqualTo(this.answer.getId())
        );
    }

    @Test
    @DisplayName("findById가 정상적으로 이루어지는지 확인")
    void Answer_select() {
        em.clear();
        Optional<Answer> answer = answerRepository.findById(this.answer.getId());
        assertThat(answer.isPresent()).isTrue();
    }

    @Test
    @DisplayName("save가 정상적으로 이루어지는지 확인")
    void Answer_save() {
        assertThat(answer.getId()).isNotNull();
    }

    @Test
    @DisplayName("update가 정상적으로 이루어지는지 확인")
    void Answer_update() {
        answer.setContents("update test");
        answerRepository.flush();
        Optional<Answer> answer = answerRepository.findById(this.answer.getId());
        assertThat(answer.get().getContents()).isEqualTo("update test");
    }

    @Test
    @DisplayName("delete가 정상적으로 이루어지는지 확인")
    void Answer_delete() {
        answerRepository.delete(answer);
        answerRepository.flush();
        Optional<Answer> answer = answerRepository.findById(this.answer.getId());
        assertThat(answer.isPresent()).isFalse();
    }

}
