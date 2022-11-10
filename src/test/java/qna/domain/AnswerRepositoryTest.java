package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("답변 저장 테스트")
    void saveTest(){
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        Question question = questionRepository.save(new Question("질문입니다", "본문입니다.").writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, "본문입니다."));

        Answer findAnswer = answerRepository.findById(answer.getId())
                .orElseGet(null);

        assertThat(findAnswer).isNotNull();
        assertThat(findAnswer).isEqualTo(answer);
    }

    @Test
    @DisplayName("특정 질문에 대한 미삭제 답변 조회 테스트")
    void findByQuestionAndDeletedFalseTest(){
        // given
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        Question question = questionRepository.save(new Question("질문입니다", "본문입니다.").writeBy(user));
        Answer answer1 = answerRepository.save(new Answer(user, question, "본문입니다."));
        Answer answer2 = answerRepository.save(new Answer(user, question, "본문입니다2."));

        // when
        List<Answer> answerList = answerRepository.findByQuestionAndDeletedFalse(question);
        List<Answer> answers = question.getAnswers();

        //then
        assertThat(answerList).isNotNull();
        assertThat(answerList).containsExactly(
                answer1, answer2
        );
        assertThat(answerList).isEqualTo(answers);
    }

    @Test
    @DisplayName("삭제되지 않은 특정 답변 조회 테스트")
    void findByIdAndDeletedFalse(){
        // given
        User user = userRepository.save(new User("user1", "user1!", "사용자1", ""));
        Question question = questionRepository.save(new Question("질문입니다", "본문입니다.").writeBy(user));
        Answer answer1 = answerRepository.save(new Answer(user, question, "본문입니다."));
        Answer answer2 = answerRepository.save(new Answer(user, question, "본문입니다2."));

        // when
        Answer findAnswer1 = answerRepository.findByIdAndDeletedFalse(answer1.getId())
                .orElseGet(null);
        Answer findAnswer2 = answerRepository.findByIdAndDeletedFalse(answer2.getId())
                .orElseGet(null);

        // then
        assertThat(findAnswer1).isNotNull();
        assertThat(findAnswer2).isNotNull();
        assertThat(findAnswer1).isEqualTo(answer1);
        assertThat(findAnswer2).isEqualTo(answer2);
    }

    @Test
    @DisplayName("특정 사용자가 답변한 내역 조회")
    void findAllByWriterTest(){
        //given
        User user = userRepository.save(new User("test11", "test11!", "테스트유저11", ""));
        Question question = questionRepository.save(new Question("질문합니다.", "본문입니다.").writeBy(user));
        Answer answer1 = answerRepository.save(new Answer(user, question, "답변입니다."));
        Answer answer2 = answerRepository.save(new Answer(user, question, "또 다른 답변입니다."));

        //when
        List<Answer> answers = answerRepository.findAllByWriter(user);
        List<Answer> answersByUser = user.getAnswers();

        //then
        assertThat(answers).containsExactly(
                answer1, answer2
        );
        assertThat(answers).isEqualTo(answersByUser);
    }
}
