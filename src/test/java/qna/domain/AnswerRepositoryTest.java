package qna.domain;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //JPA(DB) 와 관련된 Test
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;

    private User writer;
    private Question question;

    @BeforeEach
    void setting() {
        writer = userRepository.save(new User("id", "pwd", "writer", "writer@slipp.net"));
        question = questionRepository.save(QuestionTest.Q1.writeBy(writer));
    }

    @Test
    void save() {
        final Answer A1 = new Answer(writer, question, "A1");
        final Answer actual = answerRepository.save(A1); //save 에 transaction 걸려있음. IDENTITY 전략이라 insert 적용
        assertThat(actual).isEqualTo(A1);
    }

    @Test
    void findById() {
        final Answer answer = new Answer(writer, question, "AnswerIs");
        final Answer actual = answerRepository.save(answer);
        final Optional<Answer> answers = answerRepository.findById(actual.getId());
        assertThat(actual.getContents()).isEqualTo(answer.getContents());
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 리스트를 QuestionId 로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        final Answer answer1 = new Answer(writer, question, "AnswerIs1");
        final Answer answer2 = new Answer(writer, question, "AnswerIs2");
        final List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(answer1, answer2));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(answer1);

        final List<Answer> findAnswers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        assertThat(findAnswers.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제되지 않은 Answer 를 id 로 찾는다")
    void findByIdAndDeletedFalse() {
        final Answer answer1 = new Answer(writer, question, "AnswerIs1");
        final Answer answer2 = new Answer(writer, question, "AnswerIs2");
        final List<Answer> actualAnswers = answerRepository.saveAll(Arrays.asList(answer1, answer2));
        //actualAnswers.get(0).setDeleted(true);
        answerRepository.delete(answer1);

        final Optional<Answer> findAnswer = answerRepository.findByIdAndDeletedFalse(actualAnswers.get(0).getId());
        assertThat(findAnswer.isPresent()).isFalse();
    }




    @Test
    @DisplayName("cascade 학습테스트. 부모가 영속 상태이면, 자식도 영속 상태가 된다")
    void cascade() {
        //given
        User newWriter = new User("id", "pwd", "user", "email@slipp.net");
        Question newQuestion = new Question("question", "is");
        Answer newAnswer = new Answer(newWriter, newQuestion, "answer");

        newQuestion.writeBy(newWriter);         // 연관관계의 주인으로 양방향 연관관계 설정 (연관관계를 안해주면 연관관계가 실패하여 save 가 실패한다.)
        newQuestion.addAnswer(newAnswer);       // 연관관계의 주인이 아니지만, cascade 를 통해서 Answer 도 영속성
        //newAnswer.setQuestion(newQuestion);   // 연관관계의 주인이지만, 이렇게 하려면 question 도 save 하고, answer 도 save 해야 한다.
        //newWriter.addAnswer(newAnswer);       // 연관관계의 주인이 아니고, Answer 에서 User 를 넣어서 연관관계 맺어주었으므로 안해줘도 성공한다.
        //newWriter.addQuestion();              // 연관관계의 주인이 아니고, newQuestion.writeBy 으로 맺어주었으므로 안해줘도 성공한다.

        //when
        userRepository.save(newWriter);
        Question actualQuestion = questionRepository.save(newQuestion);

        //then
        //userRepository, questionRepository, answerRepository 모두 같은 영속성 컨텍스트에서 관리된다.
        assertThat(actualQuestion.getAnswer().size()).isEqualTo(1);
        assertThat(answerRepository.findById(newAnswer.getId()).get().getWriter()).isEqualTo(newWriter);
        assertThat(answerRepository.findById(newAnswer.getId())).isNotEmpty();
    }
}