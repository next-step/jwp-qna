package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.domain.AnswerTest.A1;
import static qna.domain.AnswerTest.A2;
import static qna.domain.QuestionTest.Q1;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    private Answer answer;

    @BeforeEach
    void setup() {
        User javaJigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(javaJigi));
        answer = answerRepository.save(new Answer(javaJigi, question, A1.getContents()));
        answerRepository.save(new Answer(javaJigi, question, A2.getContents()));
    }

    @DisplayName("삭제 상태가 아닌 답변을 모두 찾는다.")
    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> byId = answerRepository.findByIdAndDeletedFalse(answer.getId());
        Answer resultAnswer = byId.orElse(null);
        assertAll(
            () -> assertThat(byId).isNotEmpty(),
            () -> assertEquals(answer.getContents(), resultAnswer.getContents()),
            () -> assertEquals(answer.getWriterId(), resultAnswer.getWriterId()),
            () -> assertEquals(answer.getQuestion().getId(), resultAnswer.getQuestion().getId()),
            () -> assertThat(resultAnswer.isDeleted()).isFalse()
        );
    }

    @Test
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answerList = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());
        assertEquals(2, answerList.size());
    }
}