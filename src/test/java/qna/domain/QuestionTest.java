package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;


    private Question question;

    @BeforeEach
    void setUp() {
        question = questionRepository.save(QuestionTest.Q1.writeBy(userRepository.save(UserTest.JAVAJIGI)));
    }

    @DisplayName("동등성 비교테스트")
    @Test
    void identityTest() {
        Question savedQuestion = questionRepository.save(question);
        assertThat(questionRepository.findById(savedQuestion.getId()).get()).isSameAs(question);
    }

    @DisplayName("변경 테스트")
    @Test
    void changeTest() {
        question.setContents("testContents");
        Question changedQuestion = questionRepository.findById(question.getId()).get();
        assertThat(changedQuestion).isSameAs(question);
    }

    @DisplayName("Question 에 따른 Answers 를 가져 온다.")
    @Test
    void answersTest() {
        Question savedQuestion = questionRepository.save(question);
        Answer[] answers = saveAnswerIn(savedQuestion).toArray(new Answer[0]);
        Question question = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(question.getAnswers()).containsExactly( answers);
    }

    @DisplayName("Qustion 에서 질문을 지우면 연결관계가 끊어진다.")
    @Test
    void removeAnswerTest() {
        Question savedQuestion = questionRepository.save(question);
        List<Answer> answers1 = saveAnswerIn(savedQuestion);
        savedQuestion.removeAnswer(answers1.get(0));
        assertThat(savedQuestion.getAnswers().contains(answers1.get(0))).isFalse();
        assertThat(answerRepository.findById(answers1.get(0).getId()).get().getQuestion()).isNull();
    }

    private List<Answer> saveAnswerIn(final Question question) {
        List<Answer> answers = new ArrayList<>();
        for (int i =0; i < 2; i++) {
            Answer answer = new Answer();
            answer.toQuestion(question);
            answers.add(answerRepository.save(answer));
        }
        return answers;
    }
}
