package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer testAnswer;

    @BeforeEach
    void setUp() {
        Question savedQuestion = questionRepository.save(QuestionTest.Q1.writeBy(userRepository.save(UserTest.JAVAJIGI)));
        testAnswer = new Answer(savedQuestion.getWriter(), savedQuestion, "Answer content1");
    }

    @AfterEach
    void end() {
        QuestionTest.Q1.writeBy(UserTest.JAVAJIGI);
    }


    @DisplayName("create_at, deleted 필드 값을 null 을 가질수 없다.")
    @Test
    void createTest() {
        Answer expected = answerRepository.save(testAnswer);
        assertAll(
                () -> assertThat(expected.getCreatedAt()).isNotNull(),
                () -> assertThat(expected.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("저장값 비교하기")
    @Test
    void identityTest() {
        Answer expected = answerRepository.save(testAnswer);
        Answer answer = answerRepository.findById(expected.getId()).get();
        assertAll(
                () -> assertThat(expected.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(expected.getId()).isEqualTo(answer.getId()),
                () -> assertThat(expected.getUpdatedAt()).isEqualTo(answer.getUpdatedAt()),
                () -> assertThat(expected.getCreatedAt()).isEqualTo(answer.getCreatedAt()),
                () -> assertThat(expected.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(expected.getQuestion()).isEqualTo(answer.getQuestion())
        );
    }


    @DisplayName("변경하기 ")
    @Test
    void updateTest() {
        Answer savedAnswer = answerRepository.save(testAnswer);
        savedAnswer.setContents("Answers change");
        Answer answer = answerRepository.findById(savedAnswer.getId()).get();
        assertThat(answer.getContents()).isEqualTo(savedAnswer.getContents());
    }

    @DisplayName("답변과 사용자 간의 다대일 단방향 테스트")
    @Test
    void manyToOneAnswerAndUserTest() {
        Answer savedAnswer = answerRepository.save(testAnswer);
        assertThat(savedAnswer.getWriter()).isSameAs(testAnswer.getWriter());
    }

    @DisplayName("Answer 와 Question 간의 다대일 관계 테스트")
    @Test
    void manyToOneAnswerAndQuestionTest() {
        testAnswer = answerRepository.save(testAnswer);
        Question question = questionRepository.findById(testAnswer.getQuestion().getId()).get();
        assertThat(question.getAnswers()).contains(testAnswer);
    }

    @DisplayName("Question 변경시 기존 연결 고리가 끊어 진다.")
    @Test
    void changeQuestionTest() {
        final Question savedQuestion = questionRepository.save(QuestionTest.Q2.writeBy(userRepository.save(QuestionTest.Q2.getWriter())));
        testAnswer = answerRepository.save(testAnswer);
        final Long oldQuestionId = testAnswer.getQuestion().getId();
        testAnswer.toQuestion(savedQuestion);
        assertThat(savedQuestion.getAnswers()).contains(testAnswer);
        assertThat(questionRepository.findById(oldQuestionId).get().getAnswers().contains(testAnswer)).isFalse();
    }

}
