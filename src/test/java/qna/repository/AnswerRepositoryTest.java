package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class AnswerRepositoryTest {

    private User user;

    private Question question;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(JAVAJIGI);
        question = questionRepository.save(new Question("title1", "contents1").writeBy(user));
    }

    @Test
    @DisplayName("답변이 정상적으로 등록되었는지 확인 테스트")
    void save() {
        // given
        Answer answer1 = new Answer(user, question, "답변입니다.");
        Answer answer2 = new Answer(user, question, "답변입니다.");

        // when
        answerRepository.save(answer1);
        answerRepository.save(answer2);

        // then
        Assertions.assertThat(answerRepository.findAll()).size().isEqualTo(2);
    }

    @Test
    @DisplayName("답변 등록 후 조회 테스트")
    void find() {
        Answer original = new Answer(user, question, "답변입니다.");
        Answer result = answerRepository.save(original);

        Answer answer = answerRepository.findByIdAndDeletedFalse(result.getId()).get();

        assertAll(
                () -> Assertions.assertThat(answer).isNotNull(),
                () -> Assertions.assertThat(answer.getId()).isEqualTo(result.getId()),
                () -> Assertions.assertThat(answer.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("질문 조회시 삭제가 된 질문을 미노출 여부 테스트")
    void deletedFalse() throws CannotDeleteException {
        Answer answer = answerRepository.save(new Answer(user, question, "답변입니다."));
        answer.delete(user);

        List<Answer> result = answerRepository.findAll();
        Assertions.assertThat(result).size().isEqualTo(0);
    }
}
