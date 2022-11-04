package qna.domain.answer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import qna.NotFoundException;
import qna.domain.question.Question;
import qna.domain.question.QuestionRepository;
import qna.domain.question.QuestionTest;
import qna.domain.user.User;
import qna.domain.user.UserRepository;
import qna.domain.user.UserTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.answer.AnswerTest.A1;

@DirtiesContext
@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @Test
    @DisplayName("답변이 정상적으로 등록되있는지 테스트 한다")
    void saveAnswerTest(){
        User writerUser = userRepository.save(UserTest.createUser("user1"));
        Question question = questionRepository.save(QuestionTest.createQuestion(writerUser));
        Answer answer = answerRepository.save(new Answer(writerUser, question, "contents"));

        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(answer.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("id로 삭제되지 않은 답변 조회를 테스트한다")
    void byIdAndDeletedFalseTest(){
        Answer answer = answerRepository.save(AnswerTest.createAnswer(UserTest.createUser("user1")));
        Answer getAnswer = answerRepository.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(() -> new NotFoundException());
        assertAll(
                () -> assertThat(getAnswer).isNotNull(),
                () -> assertThat(getAnswer.getId()).isEqualTo(answer.getId()),
                () -> assertThat(getAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(getAnswer.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(getAnswer.getWriterId()).isEqualTo(answer.getWriterId()),
                () -> assertThat(getAnswer.getCreatedAt()).isEqualTo(answer.getCreatedAt()),
                () -> assertThat(getAnswer.getUpdatedAt()).isEqualTo(answer.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("답변의 삭제여부가 true로 변경되었는지 테스트한다")
    void IsDeleteChangeTest(){
        Answer answer = answerRepository.save(AnswerTest.createAnswer(UserTest.createUser("user1")));
        answer.setDeleted(true);
        Long id = answer.getId();
        Optional<Answer> byIdAndDeletedFalse = answerRepository.findByIdAndDeletedFalse(id);
        assertAll(
                () -> assertThat(byIdAndDeletedFalse).isEmpty(),
                () -> assertThat(answer.isDeleted()).isTrue()
        );

    }

    @Test
    @DisplayName("답변이 실제 삭제되었는지 테스트한다")
    void deleteByIdTest(){
        Answer answer = answerRepository.save(AnswerTest.createAnswer(UserTest.createUser("user1")));
        answerRepository.deleteById(answer.getId());
        assertAll(
                () -> assertThat(answerRepository.findById(answer.getId())).isEmpty(),
                () -> assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isEmpty()
        );
    }

}
