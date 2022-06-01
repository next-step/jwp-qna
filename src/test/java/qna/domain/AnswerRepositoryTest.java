package qna.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.AnswerTest.A1;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;


    @Test
    void 저장_테스트() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Answer expected = A1;
        Answer result = answerRepository.save(expected);

        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 아이디로_조회() {
        User user = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(QuestionTest.Q1).writeBy(user);

        Answer save = answerRepository.save(A1);
        Answer expected = answerRepository.findByIdAndDeletedFalse(user.getId()).get();

        assertThat(expected.getWriter()).isEqualTo(user);
    }


}