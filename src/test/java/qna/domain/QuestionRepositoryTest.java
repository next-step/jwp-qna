package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.QuestionTest.*;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Question firstQuestion;
    private Question secondQuestion;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(JAVAJIGI);
        User sangjigi = userRepository.save(SANJIGI);

        firstQuestion = questionRepository.save(new Question(Q1_TITLE, Q1_CONTENT).writeBy(javajigi));
        secondQuestion = questionRepository.save(new Question(Q2_TITLE, Q2_CONTENT).writeBy(sangjigi));
    }


    @Test
    @DisplayName("모든 게시글을 조회한다.")
    void findByDeletedFalse() {
        //when
        List<Question> questions = questionRepository.findByDeletedFalse();

        //then
        assertThat(questions).containsExactlyInAnyOrder(
                firstQuestion,
                secondQuestion
        );
    }
}
