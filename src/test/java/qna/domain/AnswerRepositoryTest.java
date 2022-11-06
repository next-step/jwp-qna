package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private User user;
    private Question question;

    @BeforeEach
    void setup() {
        user = userRepository.save(new User("giraffelim", "password", "sun", "email"));
        question = questionRepository.save(new Question("title", "contents"));
    }

    @Test
    void 답변_저장() {
        Answer actual = answerRepository.save(new Answer(user, question, "contents"));
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.isOwner(user)).isTrue(),
                () -> assertThat(actual.getQuestion()).isEqualTo(question),
                () -> assertThat(actual.getContents()).isEqualTo("contents"),
                () -> assertThat(actual.isDeleted()).isFalse(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 답변_저장_후_조회() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        Answer actual = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.isOwner(user)).isTrue(),
                () -> assertThat(actual.getQuestion()).isEqualTo(question),
                () -> assertThat(actual.getContents()).isEqualTo("contents"),
                () -> assertThat(actual.isDeleted()).isFalse(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void repository_의_delete_를_사용해_답변을_삭제_할_경우_예외가_발생() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        assertThatThrownBy(() -> answerRepository.deleteById(answer.getId()))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("해당 메소드를 사용해 답변을 삭제할 수 없습니다.");
    }

    @Test
    void 질문을_통한_조회() {
        answerRepository.save(new Answer(user, question, "contents"));
        List<Answer> actual = answerRepository.findByQuestionAndDeletedFalse(question);
        assertThat(actual).hasSize(1);
    }

    @Test
    void 삭제되지_않은_답변_조회() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual).isPresent();
    }

    @Test
    void 답변_수정() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        answer.changeContents("Update Contents");
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(actual.getContents()).isEqualTo("Update Contents");
    }

    @Test
    void 영속성_컨텍스트_내_동일성_비교() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        Answer actual = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(actual == answer).isTrue();
    }

    @Test
    void 질문_ID가_같을경우_같은_객체이다() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        flushAndClear();
        Answer actual = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);
        assertThat(actual).isEqualTo(answer);
    }

    @Test
    void 연관된_엔티티는_프록시_객체로_조회된다() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        flushAndClear();
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(actual.getWriter() instanceof HibernateProxy).isTrue();
        assertThat(actual.getQuestion() instanceof HibernateProxy).isTrue();
    }

    @Test
    void 답변_삭제() {
        Answer answer = answerRepository.save(new Answer(user, question, "contents"));
        answer.delete();
        flushAndClear();
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(actual.isDeleted()).isTrue();
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
