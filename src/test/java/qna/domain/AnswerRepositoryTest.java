package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setUp() {
        writer = userRepository.save(UserTest.JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
    }

    @Test
    void Answer_저장() {
        Answer answer = new Answer(writer, question, "Answers Contents1");
        Answer result = answerRepository.save(answer);

        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getContents()).isEqualTo(answer.getContents())
        );
    }

    @Test
    void Answer_단건_조회() {
        Answer result = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        assertThat(answerRepository.findById(result.getId()).get())
                .usingRecursiveComparison()
                .isEqualTo(result);
    }

    @Test
    void Answer_전체_조회() {
        answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        answerRepository.save(new Answer(writer, question, "Answers Contents2"));

        assertThat(answerRepository.findAll()).hasSize(2);
    }

    @Test
    void Answer_삭제여부_컬럼이_false인_단건_조회() {
        Answer answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).get()).isEqualTo(answer);

        Answer findAnswer = answerRepository.findById(answer.getId()).get();
        findAnswer.setDeleted(true);

        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId()).isPresent()).isFalse();

    }

    @Test
    void question_연관관계_맵핑_검증() {
        Answer answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        Answer findAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(findAnswer.getQuestion()).isEqualTo(question);
    }

    @Test
    void writer_연관관계_맵핑_검증() {
        Answer answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));
        Answer findAnswer = answerRepository.findById(answer.getId()).get();

        assertThat(findAnswer.getWriter()).isEqualTo(writer);
    }

    @Test
    void 임시_테스트() {
        User writer2 = em.persist(new User(null, "sihyun", "password", "name", "javajigi@slipp.net"));
        Question question2 = em.persist(new Question("title1", "contents1").writeBy(writer2));

        Answer answer1 = new Answer(writer2, question2, "Answers Contents1");
        Answer answer2 = new Answer(writer2, question2, "Answers Contents2");
        em.persist(answer1);
        em.persist(answer2);
        em.flush();
        em.clear();

        Answer findAnswer = em.find(Answer.class, answer1.getId());
        assertEquals(answer1, findAnswer);
    }

}
