package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
        questionRepository.save(Q1);
    }

    @Test
    void 저장() {
        Question savedQuestion = questionRepository.save(Q1);
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(Q1.getContents())
        );
    }

    @Test
    void 검색() {
        assertThat(questionRepository.findById(Q1.getId()).get())
                .isEqualTo(Q1);
    }

    @Test
    void 연관관계_유저() {
        UserTest.JAVAJIGI.addAQuestion(Q1);
        assertAll(
                () -> assertThat(Q1.getWriter()).isEqualTo(UserTest.JAVAJIGI),
                () -> assertThat(UserTest.JAVAJIGI.getQuestions().get(0)).isEqualTo(Q1)
        );
    }

    @Test
    void 연관관계_답변() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Q1.addAnswer(AnswerTest.A1);
        assertAll(
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(Q1),
                () -> assertThat(Q1.getAnswers().get(0)).isEqualTo(savedAnswer)
        );
    }

    @Test
    void cascadeTest() {
        Answer savedAnswer = answerRepository.save(AnswerTest.A1);
        Q1.addAnswer(AnswerTest.A1);
        questionRepository.delete(Q1);
        assertThat(answerRepository.findById(savedAnswer.getId()).isPresent()).isFalse();
    }

    @Test
    void 수정() {
        Q1.setContents("컨텐츠 수정");
        questionRepository.flush();
        em.clear();
        assertThat(questionRepository.findById(Q1.getId()).get().getContents()).isEqualTo(Q1.getContents());
    }

    @Test
    void 삭제() {
        Question question = questionRepository.findById(Q1.getId()).get();
        questionRepository.delete(question);
        assertThat( questionRepository.findById(Q1.getId())).isEmpty();
    }
}
