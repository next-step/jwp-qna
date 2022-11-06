package qna.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.entity.Answer;
import qna.domain.entity.Question;
import qna.domain.entity.User;
import qna.domain.repository.AnswerRepository;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private AnswerRepository answers;

    @BeforeEach
    void setUp() {
        User user = new User("diqksrk", "diqksrk", "강민준", "diqksrk123@naver.com");
        Question question = new Question("타이틀", "콘텐츠");

        answers.save(new Answer(user, question, "콘텐츠"));
    }

    @Test
    @DisplayName("answer테이블 save 테스트")
    void save() {
        User user = new User("diqksrk", "diqksrk", "강민준", "diqksrk123@naver.com");
        Question question = new Question("타이틀", "콘텐츠2");

        Answer expected = new Answer(user, question, "콘텐츠2");
        Answer actual = answers.save(expected);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("answer테이블 select 테스트")
    void findById() {
        Answer expected = answers.findByContents("콘텐츠").get();

        assertThat(expected.getContents()).isEqualTo("콘텐츠");
    }

    @Test
    @DisplayName("answer테이블 update 테스트")
    void updateDeletedById() {
        Answer expected = answers.findByContents("콘텐츠")
                .get();
        expected.setContents("콘텐츠2");
        Answer actual = answers.findByContents("콘텐츠2")
                .get();

        assertThat(actual.getContents()).isEqualTo(expected.getContents());
    }

    @Test
    @DisplayName("answer테이블 delete 테스트")
    void delete() {
        Answer expected = answers.findByContents("콘텐츠")
                .get();
        answers.delete(expected);

        assertThat(answers.findByContents("콘텐츠").isPresent()).isFalse();
    }
}