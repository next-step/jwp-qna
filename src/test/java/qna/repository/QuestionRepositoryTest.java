package qna.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.User;

import static qna.domain.UserTest.JAVAJIGI;

@DataJpaTest
class QuestionRepositoryTest {

    private User user;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = userRepository.save(JAVAJIGI);
    }

    @Test
    @DisplayName("질문을 등록한 내용이 정상적으로 등록되었는지 확인 테스트")
    void save() {
        // given
        Question question1 = new Question("title1", "contents1").writeBy(user);
        Question question2 = new Question("title1", "contents1").writeBy(user);

        // when
        questionRepository.save(question1);
        questionRepository.save(question2);

        // then
        Assertions.assertThat(questionRepository.findAll()).size().isEqualTo(2);
    }

    @Test
    @DisplayName("질문을 등록 후 답변이 정상적으로 저장 되었는지 확인 테스트")
    void find() {
        Question result = questionRepository.save(new Question("title1", "contents1").writeBy(user));

        Question question = questionRepository.findByIdAndDeletedFalse(result.getId()).get();

        Assertions.assertThat(question).isNotNull();
        Assertions.assertThat(question.getId()).isEqualTo(result.getId());
        Assertions.assertThat(question.isDeleted()).isFalse();
    }

}
