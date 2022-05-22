package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.AnswerTest.A1;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("질문아이디로 검색하여 answer객체 리스트를 반환한다.")
    void findByQuestionIdAndDeletedFalse_test() {
        //given
        User user = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, A1.getContents()));
        //when
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question.getId());
        //then
        assertThat(answers).hasSize(1);
    }

    @Test
    @DisplayName("답변아이디로 검색하여 answer객체를 반환한다.")
    void findByIdAndDeletedFalse_test() {
        //given
        User user = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(user));
        Answer answer = answerRepository.save(new Answer(user, question, A1.getContents()));
        //when
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        //then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get() == answer).isTrue()
        );
    }
}
