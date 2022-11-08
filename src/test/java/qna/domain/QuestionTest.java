package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import qna.repository.QuestionRepository;
import qna.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
public class QuestionTest {

    private static Question question;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager manager;

    @BeforeEach
    void setUp() {
        User javajigi = userRepository.save(JAVAJIGI);
        User sanjigi = userRepository.save(SANJIGI);

        question = new Question("title", "contents").writeBy(javajigi);
    }

    @Test
    @DisplayName("Question 객체를 저장하면 Id가 자동생성 되어 Not Null 이다.")
    void save() {
        assertThat(question.getId()).isNull();

        Question actual = questionRepository.save(question);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNull()
        );
    }

    @Test
    @DisplayName("Question 객체를 조회하면 데이터 여부에 따라 Optional 존재 여부가 다르다." +
            "또한 동일한 객체면 담긴 값도 동일하다.")
    void findByWriterId() {
        Question actual = questionRepository.save(question);

        assertAll(
                () -> assertThat(questionRepository.findById(actual.getId()))
                        .isPresent().get().extracting(Question::getContents).isEqualTo(question.getContents()),
                () -> assertThat(questionRepository.findById(10L)).isEmpty()
        );
    }

    @Test
    @DisplayName("Question 객체를 수정하면 수정된 데이터와 일치해야 하고 업데이트 날짜가 Not Null 이다.")
    void update() {
        Question actual = questionRepository.save(question);

        actual.setWriter(userRepository.findByUserId(SANJIGI.getUserId()).get());
        flushAndClear();

        assertAll(
                () -> assertThat(actual.getUpdatedAt()).isNotNull(),
                () -> assertThat(actual.getWriter().getUserId()).isEqualTo(SANJIGI.getUserId())
        );
    }

    private void flushAndClear() {
        manager.flush();
        manager.clear();
    }

}
