package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        assertThat(questionRepository.findById(savedQuestion.getId()).get().getAnswers1()).isEqualTo(savedQuestion.getAnswers1());
    }

    @DisplayName("로그인 사용자와 같은 경우 일때 삭제 하기")
    @Test
    void removeTest() throws CannotDeleteException {
        Question savedQuestion = questionRepository.save(question);
        DeleteHistories deleteHistories = savedQuestion.remove(savedQuestion.getWriter());
        assertThat(savedQuestion.isDeleted()).isTrue();
        answers.forEach(answer ->  {
            assertThat(answerRepository.findById(answer.getId()).get().isDeleted()).isTrue();
        });
        assertThat(deleteHistories.size()).isEqualTo(1 + answers.size());
    }

    @DisplayName("질문이 삭제된 상태 일때 삭제하면 에러가 발생한다.")
    @Test
    void invalidRemove() {
        assertThatThrownBy(() -> {
            Question savedQuestion = questionRepository.save(question);
            savedQuestion.setDeleted(true);
            savedQuestion.remove(savedQuestion.getWriter());
        }).isExactlyInstanceOf(CannotDeleteException.class);
    }
    private List<Answer> pushAnswerIn(Question question) {
        User savedUser = userRepository.save(UserTest.JAVAJIGI);
        question.setWriter(savedUser);
        List<Answer> answers = new ArrayList<>();
        Answer answer = new Answer();
        answer.setWriter(savedUser);
        answers.add(answer);
        Answer answer1 = new Answer();
        answer1.setWriter(savedUser);
        answers.add(answer1);
        answer.toQuestion(question);
        answer1.toQuestion(question);
        return answers;
    }
}
