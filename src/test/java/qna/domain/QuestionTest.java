package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private List<Answer> answers;

    @BeforeEach
    void setUp() {
        question = new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents());
        answers = pushAnswerIn(question);
    }

    @DisplayName("동등성 비교 테스트")
    @Test
    void identityTest() {
        Question savedQuestion = questionRepository.save(question);
        assertThat(questionRepository.findById(savedQuestion.getId()).get()).isSameAs(question);
    }

    @DisplayName("변경 테스트")
    @Test
    void changeTest() {
        Question savedQuestion = questionRepository.save(question);
        savedQuestion.setContents("testContents");
        assertThat(questionRepository.findById(savedQuestion.getId()).get()).isSameAs(savedQuestion);
    }

    @DisplayName("Question 에 따른 Answers 를 가져 온다.")
    @Test
    void answersTest() {
        Question savedQuestion = questionRepository.save(question);
        assertThat(questionRepository.findById(savedQuestion.getId()).get().getAnswers()).contains(answers.toArray(new Answer[0]));
    }

    @DisplayName("Question 에서 질문을 지우면 연결 관계가 끊어 진다.")
    @Test
    void removeAnswerTest() {
        Question savedQuestion = questionRepository.save(question);
        savedQuestion.removeAnswer(answers.get(0));
        assertThat(questionRepository.findById(savedQuestion.getId()).get().getAnswers().contains(answers.get(0))).isFalse();
        assertThat(answerRepository.findById(answers.get(0).getId()).get().getQuestion()).isNotEqualTo(savedQuestion);
    }

    private List<Answer> pushAnswerIn(Question question) {
        List<Answer> answers = new ArrayList<>();
        Answer answer = new Answer();
        answers.add(answer);
        Answer answer1 = new Answer();
        answers.add(answer1);
        answer.toQuestion(question);
        answer1.toQuestion(question);
        return answers;
    }
}
