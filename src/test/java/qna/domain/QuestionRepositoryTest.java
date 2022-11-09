package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @Test
    @DisplayName("질문 저장 테스트")
    void saveTest(){
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        Question saveQuestion = questionRepository.save(new Question("질문입니다.", "본문입니다.").writeBy(user));

        Question findQuestion = questionRepository.findById(saveQuestion.getId())
                .orElse(null);

        assertThat(findQuestion).isNotNull();
        assertThat(findQuestion).isEqualTo(saveQuestion);
    }

    @Test
    @DisplayName("삭제되지 않은 질문 조회 테스트")
    void findByIdAndDeletedFalseTest1(){
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        Question saveQuestion1 = questionRepository.save(new Question("질문입니다 1", "본문입니다 1").writeBy(user));
        Question saveQuestion2 = questionRepository.save(new Question("질문입니다 2", "본문입니다 2").writeBy(user));

        Question findQuestion1 = questionRepository.findByIdAndDeletedFalse(saveQuestion1.getId())
                .orElse(null);
        Question findQuestion2 = questionRepository.findByIdAndDeletedFalse(saveQuestion2.getId())
                .orElse(null);

        assertThat(findQuestion1).isNotNull();
        assertThat(findQuestion2).isNotNull();
        assertThat(findQuestion1).isEqualTo(saveQuestion1);
        assertThat(findQuestion2).isEqualTo(saveQuestion2);
    }

    @Test
    @DisplayName("삭제되지 않은 질문 조회 테스트 - 삭제된 질문은 조회되지 않는다.")
    void findByIdAndDeletedFalseTest2(){
        Question saveQuestion = questionRepository.save(new Question("질문드립니다.", "답변 부탁드립니다."));
        saveQuestion.setDeleted(true);

        Question findQuestion = questionRepository.findByIdAndDeletedFalse(saveQuestion.getId())
                .orElse(null);

        assertThat(findQuestion).isNull();
    }

    @Test
    @DisplayName("삭제되지 않은 질문 모두 조회 테스트")
    void findByDeletedFalseTest(){
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        questionRepository.save(new Question("질문입니다1", "본문입니다1").writeBy(user));
        questionRepository.save(new Question("질문입니다2", "본문입니다2").writeBy(user));
        questionRepository.save(new Question("질문드립니다.", "답변 부탁드립니다.").writeBy(user));

        List<Question> questionList = questionRepository.findByDeletedFalse();

        assertThat(questionList).isNotNull();
        assertThat(questionList).hasSize(3);
    }

    @Test
    @DisplayName("특정 작성자가 작성한 질문 모두 조회 테스트")
    void findAllByWriterTest(){
        //given
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));

        Question question1 = questionRepository.save(new Question("질문 1", "본문 1").writeBy(user));
        Question question2 = questionRepository.save(new Question("질문 2", "본문 2").writeBy(user));
        Question question3 = questionRepository.save(new Question("질문 3", "본문 3").writeBy(user));

        //when
        List<Question> questions = questionRepository.findAllByWriter(user);
        List<Question> questionsByUser = user.getQuestions();

        //then
        assertThat(questions).isNotNull();
        assertThat(questions).containsExactly(
                question1, question2,question3
        );
        assertThat(questions).isEqualTo(questionsByUser);
    }

}
