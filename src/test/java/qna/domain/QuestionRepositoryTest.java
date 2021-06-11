package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.CannotDeleteException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private EntityManager entityManager;

    private Question question1;
    private Question question2;
    private User questionWriter1;
    private User questionWriter2;

    @BeforeEach
    void setUp() {
        questionWriter1 = new User("qwriter1", "password", "name", "sunju@slipp.net");
        question1 = new Question("title3", "contents2").writeBy(questionWriter1);
        userRepository.save(questionWriter1);

        questionWriter2 = new User("qwriter2", "password", "name", "jung@slipp.net");
        question2 = new Question("title3", "contents2").writeBy(questionWriter2);
        userRepository.save(questionWriter2);
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // 데이터 테스트
        assertThat(question1.getId()).isNull();
        Question savedQuestion = questionRepository.save(question1);
        questionRepository.flush();
        assertThat(savedQuestion.getId()).isNotNull();
        assertThat(savedQuestion.getTitle()).isEqualTo(question1.getTitle());
        assertThat(savedQuestion.getContents()).isEqualTo(question1.getContents());
        assertThat(savedQuestion.getCreatedAt()).isNotNull();
        assertThat(savedQuestion.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Question 여러개 save 테스트")
    void saveMultipleQuestionTest() {
        Question savedQuestion1 = questionRepository.save(question1);
        Question savedQuestion2 = questionRepository.save(question2);
        questionRepository.flush();
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList.size()).isEqualTo(2);
        assertThat(questionList).containsExactly(savedQuestion1, savedQuestion2);
    }



    @Test
    @DisplayName("삭제되지 않은 질문 검색 테스트")
    void findByDeletedFalseTest() {
        Question savedQuestion1 = questionRepository.save(question1);
        Question savedQuestion2 = questionRepository.save(question2);
        questionRepository.flush();
        List<Question> actualList = questionRepository.findByDeletedFalse();

        //findByDeletedFalse test
        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList).contains(savedQuestion1, savedQuestion2);

        //deleted false test
        for(Question question : actualList) {
            assertThat(question.isDeleted()).isEqualTo(false);
        }
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalseTest() {
        Question savedQuestion = questionRepository.save(question1);
        questionRepository.flush();
        Question actual = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId())
                                            .orElseThrow(IllegalArgumentException::new);

        assertThat(actual).isEqualTo(savedQuestion);
        assertThat(actual.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("작성자 확인 테스트")
    void isWrittenByTest() {
        Question savedQuestion = questionRepository.save(question1);
        assertThat(savedQuestion.isOwner(questionWriter1));
    }

    @Test
    @DisplayName("단일 질문 삭제 테스트")
    void deleteSingleQuestionTest() throws CannotDeleteException {
        Question savedQuestion = questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.flush();
        List<Question> beforeDeleteList = questionRepository.deletedFalse();
        assertThat(beforeDeleteList.size()).isEqualTo(2);

        savedQuestion.delete(questionWriter1);
        questionRepository.flush();
        List<Question> afterDeleteList = questionRepository.deletedFalse();
        assertThat(afterDeleteList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("질문, 답변 삭제 테스트_실패")
    void deleteQuestionAndAnswerTest_shouldBeFail() throws CannotDeleteException {
        Question savedQuestion = questionRepository.save(question1);

        User answerWriter = new User("awriter", "password", "name", "sunju@slipp.net");
        userRepository.save(answerWriter);
        Answer answer = new Answer(answerWriter, savedQuestion, "Answers Contents");
        savedQuestion.addAnswer(answer);
        answerRepository.save(answer);

        assertThatThrownBy(() -> savedQuestion.delete(answerWriter))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    @DisplayName("질문, 답변 삭제 테스트_성공")
    void deleteQuestionAndAnswerTest_shouldBeSuccess() throws CannotDeleteException {
        Question savedQuestion = questionRepository.save(question1);

        Answer answer = new Answer(questionWriter1, savedQuestion, "Answers Contents");
        savedQuestion.addAnswer(answer);
        answerRepository.save(answer);

        savedQuestion.delete(questionWriter1);

        List<Question> questionList = questionRepository.deletedFalse();
        assertThat(questionList.size()).isEqualTo(0);
        List<Answer> answerList = answerRepository.deletedFalse();
        assertThat(answerList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Answer list 조회 테스트")
    void getAnswerList() {
        Question savedQuestion = questionRepository.save(question1);
        questionRepository.save(question2);

        User answerWriter = new User("awriter", "password", "name", "sunju@slipp.net");
        userRepository.save(answerWriter);
        Answer answer = new Answer(answerWriter, savedQuestion, "Answers Contents");
        answerRepository.save(answer);

        entityManager.clear();

        Question updatedQuestion = questionRepository.findById(savedQuestion.getId()).get();
        assertThat(updatedQuestion.getAnswers().asList().size()).isEqualTo(1);
    }
}
