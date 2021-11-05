package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@Transactional
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1");
    public static final Question Q2 = new Question("title2", "contents2");
    private User javajigi;
    private User sanjigi;
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        javajigi = userRepository.save(UserTest.JAVAJIGI);
        sanjigi = userRepository.save(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("질문 저장 테스트")
    void saveQuestionTest() {
        Question savedQ1 = questionRepository.save(Q1.writeBy(javajigi));
        Question savedQ2 = questionRepository.save(Q2.writeBy(sanjigi));

        assertAll(
                () -> assertThat(savedQ1.getId()).isNotNull(),
                () -> assertThat(savedQ2.getTitle()).isEqualTo(Q2.getTitle())
        );
    }

    @Test
    @DisplayName("질문 목록 찾기 테스트")
    void findByDeletedFalseTest() {
        Question newQ1 = questionRepository.save(Q1.writeBy(javajigi));
        Question newQ2 = questionRepository.save(Q2.writeBy(sanjigi));

        List<Question> questions = questionRepository.findByDeletedFalse();
        assertThat(questions).contains(newQ1, newQ2);
    }

    @Test
    @DisplayName("질문 ID로 질문 찾기 테스트")
    void findByIdAndDeletedFalseTest() {
        Question savedQ1 = questionRepository.save(Q1.writeBy(javajigi));
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(savedQ1.getId());
        assertThat(questionOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("질문ID가 없는 데이터로 찾는 테스트")
    void notFindIdAndDeletedFalseTest() {
        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(3L);
        assertThat(questionOptional.isPresent()).isFalse();
    }

    @Test
    @DisplayName("질문에 있는 답변 찾기")
    public void getAnswersTest() {
        Question savedQ1 = questionRepository.save(Q1.writeBy(javajigi));

        Answer newAnswer1 = new Answer(1L, javajigi, savedQ1, "Answers Contents1");
        Answer newAnswer2 = new Answer(2L, sanjigi, savedQ1, "Answers Contents2");

        answerRepository.save(newAnswer1);
        answerRepository.save(newAnswer2);

        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(savedQ1.getId());
        assertThat(answers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("질문 삭제 테스트")
    void deleteQuestionTest() throws CannotDeleteException {
        Question savedQ1 = questionRepository.save(Q1.writeBy(javajigi));

        Answer newAnswer1 = new Answer(1L, javajigi, savedQ1, "Answers Contents1");
        Answer newAnswer2 = new Answer(2L, sanjigi, savedQ1, "Answers Contents2");

        answerRepository.save(newAnswer1);
        answerRepository.save(newAnswer2);

        List<DeleteHistory> deletedHistory = savedQ1.delete(javajigi);

        assertThat(deletedHistory.size()).isEqualTo(3);


    }
}
