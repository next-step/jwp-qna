package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(Q1);
        questionRepository.save(Q2);
    }


    @Test
    @DisplayName("answer id로 deleted 제거가 되지 않은것을 찾는다.")
    void findByIdAndDeletedFalse() {
        final Answer a1 = answerRepository.save(A1);
        answerRepository.save(A2);

        Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(a1.getId());

        assertAll(() -> {
            assertThat(byIdAndDeletedFalse.isPresent()).isTrue();
            assertThat(byIdAndDeletedFalse.get()).isEqualTo(a1);
        });

    }

    @Test
    @DisplayName("전체 데이터를 찾는다.")
    @Transactional
    void findAll() {
        answerRepository.save(A1);
        answerRepository.save(A2);

        List<Answer> list = answerRepository.findAll();

        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("해당 id가 아닌 값들을 찾는다.")
    void findByIdNot() {
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        List<Answer> list = answerRepository.findByIdNot(a1.getId());

        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo(a2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"변경 컨텐츠", "컨텐츠 변경"})
    @DisplayName("해당  Content 값이 변경된다 변경된다.")
    void changeContent(String text) {
        Answer answer = answerRepository.save(A1);
        answerRepository.flush();

        Answer find = answerRepository.findById(answer.getId()).orElseGet(Answer::new);
        find.setContents(text);

        assertThat(answerRepository.findById(answer.getId()).orElseGet(Answer::new)
                .getContents()).isEqualTo(text);

    }


    @Test
    @DisplayName("삭제 되지 질문의 응답을 구한다")
    void findByQuestionAndDeletedFalse() {
        Answer a1 = answerRepository.save(A1);
        Answer a2 = answerRepository.save(A2);

        assertThat(answerRepository.findByQuestionAndDeletedFalse(a1.getQuestion()))
                .hasSize(2);
    }

    @Test
    @DisplayName("삭제 테스트")
    void delete() {
        Answer a1 = answerRepository.save(A1);
        answerRepository.flush();

        answerRepository.delete(a1);
        answerRepository.flush();

        assertThat(answerRepository.findById(a1.getId()).isPresent())
                .isFalse();
    }

    @Test
    @DisplayName("생성 테스트")
    void create() {
        Answer save = answerRepository.save(A1);

        assertThat(answerRepository.findById(save.getId()).isPresent()).isTrue();
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void saveAnswer() {
        final User testUser = userRepository.save(
                new User("userId", "password", "name", "email"));
        final Question q1 = new Question("title1", "contents1").writeBy(testUser);

        questionRepository.save(q1);

        answerRepository.save(new Answer(testUser, q1, "답변1"));
        answerRepository.save(new Answer(testUser, q1, "답변2"));
        answerRepository.save(new Answer(testUser, q1, "답변3"));
        em.clear();

        final Optional<Question> findQ1 = questionRepository.findByIdAndDeletedFalse(q1.getId());


        assertAll(
                () -> assertThat(findQ1).isPresent(),
                () -> assertThat(findQ1.get().getAnswers()).hasSize(3)
        );
    }
}
