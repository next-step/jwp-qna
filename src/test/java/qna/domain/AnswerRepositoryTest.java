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


    @BeforeEach
    public void setUp() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        User sanjigi = userRepository.save(UserTest.SANJIGI);
        questionRepository.save(
            new Question(QuestionTest.Q1.getTitle(), QuestionTest.Q1.getContents(), javajigi));
        questionRepository
            .save(new Question(QuestionTest.Q2.getTitle(), QuestionTest.Q2.getContents(), sanjigi));
    }

    @AfterEach
    public void tearDown() {
        answerRepository.deleteAll();
        userRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("자바지기 사용자가 질문에 대한 답변 등록 성공")
    public void saveAnswerByJavajigiSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question question = questionRepository.findByDeletedFalse().get(0);
        Answer answer = new Answer(javajigi, question, "answer1");
        Answer save = answerRepository.save(answer);

        assertAll(() -> {
            assertThat(save.getWriter().equalsNameAndEmail(answer.getWriter()));
            assertThat(save.getQuestion().equals(answer.getQuestion()));
        });

    }

    @Test
    @DisplayName("답변 찾기 by id 성공")
    public void findByIdSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question question = questionRepository.findByDeletedFalse().get(0);
        Answer answer = new Answer(javajigi, question, "answer1");
        answerRepository.save(answer);
        Optional<Answer> optionalAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId());

        assertAll(() -> {
            assertThat(optionalAnswer.isPresent()).isTrue();
            Answer findAnswer = optionalAnswer.get();
            assertThat(findAnswer.getQuestion().equals(answer.getQuestion()));
        });

    }

    @Test
    @DisplayName("답변 찾기 by question id 성공")
    public void findByQuestionIdSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question question = questionRepository.findByDeletedFalse().get(0);
        Answer answer1 = new Answer(javajigi, question, "answer1");
        Answer answer2 = new Answer(javajigi, question, "answer2");
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        List<Answer> answers = answerRepository
            .findByQuestionIdAndDeletedFalse(question.getId());

        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("답변이 해당 사용자의 답변인지 체크 성공")
    public void isOwnerSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question question = questionRepository.findByDeletedFalse().get(0);
        Answer answer = new Answer(javajigi, question, "answer1");
        Answer save = answerRepository.save(answer);
        assertThat(save.isOwner(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변 삭제 플래그 true 변경 성공")
    public void updateDeletedSuccess() {
        User javajigi = userRepository.findByUserId(UserTest.JAVAJIGI.getUserId()).get();
        Question question = questionRepository.findByDeletedFalse().get(0);
        Answer answer = new Answer(javajigi, question, "answer1");
        Answer save = answerRepository.save(answer);

        save.setDeleted(true);
        Answer deleted = answerRepository.save(save);

        assertAll(() -> {
            assertThat(deleted.getId()).isEqualTo(save.getId());
            assertThat(deleted.isDeleted()).isTrue();
        });
    }

}