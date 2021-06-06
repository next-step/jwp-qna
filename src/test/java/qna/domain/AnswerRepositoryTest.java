package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question expectedQuestion;
    private Answer answer1;
    private Answer answer2;
    private User questionWriter;
    private User answerWriter1;
    private User answerWriter2;

    @BeforeEach
    void setUp() {
        questionWriter = new User("qwriter", "password", "name", "sunju@slipp.net");
        expectedQuestion = new Question("title3", "contents2").writeBy(questionWriter);
        userRepository.save(questionWriter);
        questionRepository.save(expectedQuestion);

        answerWriter1 = new User("awriter", "password", "name", "sunju@slipp.net");
        answer1 = new Answer(answerWriter1, expectedQuestion, "Answers Contents");
        userRepository.save(answerWriter1);

        answerWriter2 = new User("awriter2", "password", "name", "sunju@slipp.net");
        answer2 = new Answer(answerWriter2, expectedQuestion, "Answers Contents2");
        userRepository.save(answerWriter2);
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        assertThat(AnswerTest.ANSWER1.getId()).isNull();
        Answer actualAnswer = answerRepository.save(answer1);
        answerRepository.flush();
        assertThat(actualAnswer.getId()).isNotNull(); // id 생성 테스트
        assertThat(actualAnswer.getWriter()).isEqualTo(answer1.getWriter());
        assertThat(actualAnswer.getQuestion()).isEqualTo(answer1.getQuestion());
        assertThat(actualAnswer.getContents()).isEqualTo(answer1.getContents());
        assertThat(actualAnswer.getCreatedAt()).isNotNull();
        assertThat(actualAnswer.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Answer 여러개 save 테스트")
    void saveMultipleAnswerTest() {
        Answer savedAnswer1 = answerRepository.save(answer1);
        Answer savedAnswer2 = answerRepository.save(answer2);
        answerRepository.flush();
        List<Answer> answerList = answerRepository.findAll();
        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList).containsExactly(savedAnswer1, savedAnswer2);
    }

    @Test
    @DisplayName("question id 기준 검색 테스트")
    void findByQuestionAndDeletedFalseTest() {
        Answer savedAnswer1 = answerRepository.save(answer1);
        Answer savedAnswer2 = answerRepository.save(answer2);
        answerRepository.flush();
        List<Answer> actualList = answerRepository.findByQuestionIdAndDeletedFalse(answer1.getQuestion().getId());

        //findByQuestionId test
        assertThat(actualList.size()).isEqualTo(2);
        assertThat(actualList).contains(savedAnswer1, savedAnswer2);

        //deleted false test
        for(Answer answer : actualList) {
            assertThat(answer.isDeleted()).isEqualTo(false);
        }
    }

    @Test
    @DisplayName("id 기준 검색 테스트")
    void findByIdAndDeletedFalseTest() {
        Answer savedAnswer1 = answerRepository.save(answer1);
        answerRepository.flush();

        Answer answer = answerRepository.findByIdAndDeletedFalse(savedAnswer1.getId())
                .orElseThrow(IllegalArgumentException::new);
        assertThat(answer).isEqualTo(savedAnswer1); // findById test
        assertThat(answer.isDeleted()).isEqualTo(false); //deleted false test

    }

    @Test
    @DisplayName("작성자 확인 테스트")
    void isWrittenByTest() {
        Answer savedAnswer = answerRepository.save(answer1);
        assertThat(savedAnswer.isOwner(answerWriter1));
    }

    @Test
    @DisplayName("답변 삭제 테스트")
    void deleteAnswerTest() throws CannotDeleteException {
        Answer savedAnswer1 = answerRepository.save(answer1);
        answerRepository.save(answer2);
        answerRepository.flush();
        List<Answer> beforeDeleteList = answerRepository.deletedFalse();
        assertThat(beforeDeleteList.size()).isEqualTo(2);

        savedAnswer1.delete(answerWriter1);
        answerRepository.flush();
        List<Answer> afterDeleteList = answerRepository.deletedFalse();
        assertThat(afterDeleteList.size()).isEqualTo(1);
    }
}
