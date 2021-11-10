package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private User javajigi;
//    private User sanjigi;
    private Question question;

    @BeforeEach
    public void setUp() {
        javajigi = userRepository.save(new User("javajigi", "password", "javajigi", "email"));
//        sanjigi = userRepository.save(UserTest.SANJIGI);
        question = questionRepository.save(QuestionTest.Q1);
    }

    @AfterEach
    public void tearDown() {
        answerRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("자바지기 사용자가 질문에 대한 답변 등록 성공")
    public void saveAnswerByJavajigiSuccess() {
        Answer answer = new Answer(javajigi, question, "answer1");
        answer.setWriter(javajigi);
        answer.toQuestion(question);
        Answer save = answerRepository.save(answer);

        assertAll(() -> {
            assertThat(save.getWriter().equalsNameAndEmail(answer.getWriter()));
            assertThat(save.getQuestion().equalsId(answer.getQuestion()));
        });

    }

    @Test
    @DisplayName("답변 찾기 by id 성공")
    public void findByIdSuccess() {
        Answer answer = new Answer(javajigi, question, "answer1");
        answer.setWriter(javajigi);
        answer.toQuestion(question);
        answerRepository.save(answer);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertAll(() -> {
            assertThat(optionalAnswer.isPresent()).isTrue();
            Answer findAnswer = optionalAnswer.get();
            assertThat(findAnswer.getQuestion().equalsId(answer.getQuestion()));
        });

    }

    @Test
    @DisplayName("답변 찾기 by question id 성공")
    public void findByQuestionIdSuccess() {
        Answer answer1 = AnswerTest.A1;
        answer1.setWriter(javajigi);
        answer1.toQuestion(question);
        Answer answer2 = AnswerTest.A2;
        answer2.setWriter(javajigi);
        answer2.toQuestion(question);
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        List<Answer> answers = answerRepository
            .findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변이 해당 사용자의 답변인지 체크 성공")
    public void isOwnerSuccess() {
        Answer answer = new Answer(javajigi, QuestionTest.Q1, "answer1");
        answer.setWriter(javajigi);
        Answer save = answerRepository.save(answer);
        assertThat(save.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변 삭제 플래그 true 변경 성공")
    public void updateDeletedSuccess() {
        Answer answer = new Answer(javajigi, question, "answer1");
        answer.setWriter(javajigi);
        answer.toQuestion(question);
        Answer save = answerRepository.save(answer);

        save.setDeleted(true);
        Answer deleted = answerRepository.save(save);

        assertAll(() -> {
            assertThat(deleted.getId()).isEqualTo(save.getId());
            assertThat(deleted.isDeleted()).isTrue();
        });
    }

}