package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.saveAll(Arrays.asList(JAVAJIGI, SANJIGI));
        questionRepository.saveAll(Arrays.asList(Q1, Q2));
    }

    @Test
    @DisplayName("답변을 저장할 수 있어야 한다.")
    void save() {
        Answer answer = A1;
        Answer savedAnswer = answerRepository.save(answer);
        assertThat(savedAnswer.getId()).isNotNull();

        assertAll(
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(userRepository.findById(A1.getWriterId()).get()),
                () -> assertThat(savedAnswer.getCreatedAt()).isNotNull());
    }

//    @Test
//    @DisplayName("AnswerId로 삭제상태가 아닌 Answer를 가져올 수 있어야 한다.")
//    void findByIdAndDeletedFalse() {
//        answerRepository.save(A1);
//
//        Optional<Answer> answer = answerRepository.findByIdAndDeletedFalse(A1.getId());
//
//        assertThat(answer).contains(A1);
//    }

    @DisplayName("toQuestion을 이용해 Answer에 대한 Question을 변경한다.")
    @Test
    void modifyQuestion() {
        final Answer answer = new Answer(UserTest.SANJIGI, Q1, "test");
        Question question = new Question(1L, "test title", "test contents");
        answer.toQuestion(question);
        final Answer saved = answerRepository.save(answer);
        assertThat(saved.getQuestion()).isEqualTo(question);
    }

    @Test
    @DisplayName("삭제 상태가 아닌 결과를 리턴한다.")
    void findByQuestionIdAndDeleted() {
//        List<Answer> answers = answerRepository.saveAll(Arrays.asList(A1, A2));
//
//        Question question = questionRepository.findByIdAndDeletedFalse(A1.getQuestionId()).get();
//        List<Answer> savedAnswers = question.getAnswers();
//
//        savedAnswers.forEach(answer -> {
//            assertThat(answer.isDeleted()).isEqualTo(false);
//        });

        User questionWriter = userRepository.findByUserId(JAVAJIGI.getUserId()).get();
        Question savedQuestion = questionRepository.findByIdAndDeletedFalse(Q1.getId()).get();
        savedQuestion.writeBy(questionWriter);

        User answerWriter = userRepository.findByUserId(SANJIGI.getUserId()).get();
        Answer savedAnswer1 = answerRepository.save(new Answer(answerWriter, savedQuestion, "answerContents1"));
        Answer savedAnswer2 = answerRepository.save(new Answer(answerWriter, savedQuestion, "answerContents2"));

        Question findQuestion = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())
                .orElseThrow(RuntimeException::new);

        List<Answer> findAnswers = findQuestion.getAnswers();
        assertThat(findAnswers).hasSize(2);
        assertThat(findAnswers).containsExactly(savedAnswer1, savedAnswer2);
    }
}
