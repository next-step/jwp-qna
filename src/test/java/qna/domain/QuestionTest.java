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

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    EntityManager em;
    User user1;
    Question question1;
    Answer answer1;

    @BeforeEach
    void init() {
        user1 = new User("javajigi", "password", "name", "javajigi@slipp.net");
        question1 = new Question("title1", "contents1").writeBy(user1);
        answer1 = new Answer(user1, question1, "Answers Contents1");
        userRepository.save(user1);
        questionRepository.save(question1);
    }

    @Test
    void 저장() {
        Question foundQuestion = questionRepository.findById(question1.getId()).get();
        assertAll(
                () -> assertThat(question1.getId()).isNotNull(),
                () -> assertThat(question1.getContents()).isEqualTo(foundQuestion.getContents())
        );
    }

    @Test
    void 검색() {
        Question foundQuestion = questionRepository.findById(question1.getId()).get();
        assertThat(questionRepository.findById(question1.getId()).get())
                .isEqualTo(foundQuestion);
    }

    @Test
    void 연관관계_유저_조회() {
        em.flush();
        em.clear();
        User foundUser = userRepository.findById(user1.getId()).get();
        assertAll(
                () -> assertThat(question1.getWriter()).isEqualTo(user1),
                () -> assertThat(foundUser.getQuestions().get(0).getId()).isEqualTo(question1.getId())
        );
    }

    @Test
    void 연관관계_답변_조회() {
        Answer savedAnswer = answerRepository.save(answer1);
        question1.addAnswer(savedAnswer);
        em.flush();
        em.clear();
        Answer foundAnswer = answerRepository.findById(savedAnswer.getId()).get();
        assertAll(
                () -> assertThat(foundAnswer.getQuestion().getId()).isEqualTo(question1.getId()),
                () -> assertThat(question1.getAnswers().get(0).getId()).isEqualTo(foundAnswer.getId())
        );
    }

    @Test
    void cascadeTest() {
        Answer savedAnswer = answerRepository.save(answer1);
        question1.addAnswer(savedAnswer);
        questionRepository.delete(question1);
        assertThat(answerRepository.findById(savedAnswer.getId()).isPresent()).isFalse();
    }

    @Test
    void 수정() {
        question1.setContents("컨텐츠 수정");
        questionRepository.flush();
        em.clear();
        assertThat(questionRepository.findById(question1.getId()).get().getContents()).isEqualTo(question1.getContents());
    }

    @Test
    void 삭제() {
        questionRepository.delete(question1);
        assertThat(questionRepository.findById(question1.getId())).isEmpty();
    }
}
