package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

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
        Answer answer1 = answerRepository.save(new Answer(user, question, "답변입니다."));

        Answer answer = answerRepository.findByIdAndDeletedFalse(answer1.getId()).get();

        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.getId()).isEqualTo(answer1.getId());
        Assertions.assertThat(answer.isDeleted()).isFalse();
    }

}
