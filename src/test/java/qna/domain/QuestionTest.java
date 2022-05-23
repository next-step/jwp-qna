package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final List<Answer> answers = Arrays.asList(AnswerTest.A1, AnswerTest.A2);

    @BeforeEach
    void setUp() {
        saveUserBy(answers);
    }

    @DisplayName("동등성 비교테스트")
    @Test
    void identityTest() {
        Question savedQuestion = questionRepository.save(Q1);
        Optional<Question> isQuestion = questionRepository.findById(savedQuestion.getId());
        assertThat(isQuestion.isPresent()).isTrue();
        assertThat(isQuestion.get()).isSameAs(savedQuestion);
    }

    @DisplayName("변경 테스트")
    @Test
    void changeTest() {
        Question savedQuestion = questionRepository.save(Q1);
        savedQuestion.setContents("testContents");
        Question changedQuestion = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(changedQuestion).isSameAs(savedQuestion);
    }

    @DisplayName("Question 에 따른 Answers 를 가져 온다.")
    @Test
    void answersTest() {
        Question savedQuestion = questionRepository.save(Q1);
        saveAnswerIn(savedQuestion);
        Question question = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(question.getAnswers()).contains(AnswerTest.A1, AnswerTest.A2);
    }

    @DisplayName("Qustion 에서 질문을 지우면 연결관계가 끊어진다.")
    @Test
    void removeAnswerTest() {
        Question savedQuestion = questionRepository.save(Q1);
        List<Answer> answers1 = saveAnswerIn(savedQuestion);
        savedQuestion.removeAnswer(answers1.get(0));
        assertThat(savedQuestion.getAnswers().contains(answers1.get(0))).isFalse();
        assertThat(answerRepository.findById(answers1.get(0).getId()).get().getQuestion()).isNull();
    }

    private List<Answer> saveAnswerIn(final Question question) {
        return answers.stream().map((answer) -> {
            answer.toQuestion(question);
            return answerRepository.save(answer);
        }).collect(Collectors.toList());
    }

    private void saveUserBy(List<Answer> answers) {
        answers.forEach((answer) -> {
            User writer = answer.getWriter();
            writer.setId(null);
            answer.setWriter(userRepository.save(writer));
        });
    }
}
