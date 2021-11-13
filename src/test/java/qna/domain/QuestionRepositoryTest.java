package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save() {
        User savedUser = userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.setWriter(savedUser);
        Question savedQuestion = questionRepository.save(TestDummy.QUESTION1);

        assertAll(
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(TestDummy.QUESTION1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(TestDummy.QUESTION1.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(TestDummy.QUESTION1.getWriter()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(TestDummy.QUESTION1.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }

    @Test
    void read() {
        User savedUser = userRepository.save(TestDummy.USER_SANJIGI);
        TestDummy.QUESTION1.setWriter(savedUser);
        Long savedId = questionRepository.save(TestDummy.QUESTION1).getId();
        Question savedQuestion = questionRepository.findByIdAndDeletedFalse(savedId).get();

        assertAll(
            () -> assertThat(savedQuestion.getId()).isEqualTo(savedId),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(TestDummy.QUESTION1.getTitle()),
            () -> assertThat(savedQuestion.getContents()).isEqualTo(TestDummy.QUESTION1.getContents()),
            () -> assertThat(savedQuestion.getWriter()).isEqualTo(TestDummy.QUESTION1.getWriter()),
            () -> assertThat(savedQuestion.isDeleted()).isEqualTo(TestDummy.QUESTION1.isDeleted()),
            () -> assertThat(savedQuestion.getCreatedDateTime()).isBefore(LocalDateTime.now())
        );
    }
}
