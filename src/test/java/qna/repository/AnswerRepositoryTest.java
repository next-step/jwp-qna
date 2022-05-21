package qna.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.repository.entity.Answer;
import qna.repository.entity.Question;
import qna.repository.entity.User;
import qna.repository.entity.UserTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static qna.repository.entity.AnswerTest.*;
import static qna.repository.entity.QuestionTest.Q1;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired()
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    private Answer answer;

    @BeforeEach
    void setup() {
        User javajigi = userRepository.save(UserTest.JAVAJIGI);
        Question question = questionRepository.save(new Question(Q1.getTitle(), Q1.getContents()).writeBy(javajigi));
        answer = answerRepository.save(new Answer(javajigi, question, A1.getContents()));
        answerRepository.save(new Answer(javajigi, question, A2.getContents()));
    }

    @Test()
    @DisplayName("정상 상태의 답변을 ID로 찾는다")
    void findByIdAndDeletedFalse() {
        Optional<Answer> find = answerRepository.findByIdAndDeletedFalse(answer.getId());
        Answer actual = find.orElse(null);

        assertAll(
                () -> assertThat(find).isNotEmpty(),
                () -> assertEquals(answer.getContents(), actual.getContents()),
                () -> assertEquals(answer.getWriterId(), actual.getWriterId()),
                () -> assertEquals(answer.getQuestion().getId(), actual.getQuestion().getId()),
                () -> assertThat(actual.isDeleted()).isFalse()
        );
    }

    @Test()
    @DisplayName("정상 상태의 답변을 Question ID로 찾는다")
    void findByQuestionIdAndDeletedFalse() {
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(answer.getQuestion().getId());

        assertEquals(2, answers.size());
    }
}
