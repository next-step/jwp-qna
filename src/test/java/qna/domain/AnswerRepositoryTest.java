package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class AnswerRepositoryTest extends NewEntityTestBase{

    private Answer A1;
    private Answer A2;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private EntityManager em;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        A1 = new Answer(NEWUSER1, Q1, "Answers Contents1");
        A2 = new Answer(NEWUSER2, Q2, "Answers Contents2");
        answerRepository.saveAll(Arrays.asList(A1,A2));
    }

    @Test
    @DisplayName("연관관계를 맺으면 정상적으로 저장이 되는 것을 확인")
    void test1() {
        User user = new User("id","pass","name","email");
        Question question = new Question("title","contents").writeBy(user);
        Answer cascade_test = new Answer(user, question, "cascade test");
        Answer save = answerRepository.save(cascade_test);

        assertAll(
                () -> assertThat(save.getQuestion().getWriter().getId()).isPositive(),
                () -> assertThat(save.getWriter()).isSameAs(save.getQuestion().getWriter()),
                () -> assertThat(save.getWriter().getUserId()).isEqualTo(user.getUserId()),
                () -> assertThat(save.getQuestion().getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(save.getQuestion().getId()).isPositive()
        );
    }

    @Test
    @DisplayName("Question Id와 삭제되지 않은 Answer를 검색하면  두 건이 조회됨")
    void findByQuestionQIdAndDeletedFalse() {
        Answer deletedAnswer = new Answer(NEWUSER1, Q2, "some contents");
        deletedAnswer.setDeleted(true);

        answerRepository.save(deletedAnswer);
        List<Answer> answers = answerRepository.findByQuestionAndDeletedFalse(Q2);

        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Question Id와 삭제되지 않은 Answer를 검색하면 한 건이 조회됨")
    void findByDeletedFalse() {
        A1.setDeleted(true);
        A2.setDeleted(false);

        List<Answer> answers = answerRepository.findByDeletedTrue();

        assertThat(answers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("저장된 Answer를 검색하면 동일한 Entity가 반환됨")
    void findByIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.saveAll(Arrays.asList(A1, A2));

        Optional<Answer> result = answerRepository.findByIdAndDeletedFalse(answers.get(0).getId());

        assertThat(result.get()).isSameAs(answers.get(0));
    }

    @Test
    @DisplayName("쿼리 메소드로 마지막에 생성된 answer를 조회함")
    void findTopByOrderByIdDesc() {
        answerRepository.saveAll(Arrays.asList(A1, A2));

        Optional<Answer> lastAnswer = answerRepository.findTopByOrderByIdDesc();

        assertThat(lastAnswer.get()).isSameAs(A2);
    }

    @Test
    @DisplayName("content가 업데이트 된다. dynamicUpdate적용으로 변경된 컬럼만 쿼리에 포함됨.")
    void update() {
        Answer save = answerRepository.save(A1);
        save.setContents("updated");

        em.flush();
        em.clear();

        Optional<Answer> updated = answerRepository.findById(save.getId());

        assertThat(updated.get().getContents()).isEqualTo("updated");
    }

    @Test
    @DisplayName("contains 로 % ~ % 검색이 가능함")
    void findByRegEx() {
        A1.setContents("12345");
        A2.setContents("abcde");
        answerRepository.saveAll(Arrays.asList(A1, A2));

        List<Answer> matchesRegex = answerRepository.findByContentsContains("123");

        assertAll(
                () -> assertThat(matchesRegex.size()).isEqualTo(1),
                () -> assertThat(matchesRegex.get(0).getContents()).isEqualTo("12345")
        );
    }
}