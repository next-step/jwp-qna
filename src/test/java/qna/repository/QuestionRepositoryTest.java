package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
public class QuestionRepositoryTest {
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("삭제가 false인 질문리스트 길이는 2이다.")
    void findByDeletedFalse_test() {
        //given
        User user = userRepository.save(JAVAJIGI);
        questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(user));
        //when
        List<Question> questions = questionRepository.findByDeletedFalse();
        //then
        assertThat(questions).hasSize(1);
    }

    @Test
    @DisplayName("질문 아이디로 삭제가 false인 객체를 검색하여 질문 객체를 반환한다.")
    void findByIdAndDeletedFalse() {
        //given
        User user = userRepository.save(JAVAJIGI);
        Question question = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(user));
        //when
        Optional<Question> actual = questionRepository.findByIdAndDeletedFalse(question.getId());
        //then
        assertAll(
                () -> assertThat(actual.isPresent()).isTrue(),
                () -> assertThat(actual.get() == question).isTrue()
        );

    }
}
