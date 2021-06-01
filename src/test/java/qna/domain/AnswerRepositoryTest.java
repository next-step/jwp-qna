package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;

    @Autowired
    private UserRepository users;

    @Autowired
    private QuestionRepository questions;

    @BeforeEach
    void setUp() {
        users.save(UserTest.JAVAJIGI);
        users.save(UserTest.SANJIGI);
        users.flush();
        questions.save(QuestionTest.Q1);
        questions.save(QuestionTest.Q2);
        questions.flush();
    }

    @DisplayName("Answer 데이터를 저장후 인서트가 이루어 졌는지 데이터 확인")
    @Test
    void save() {
        Answer actual = answers.save(AnswerTest.A1);
        answers.flush();
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(actual.getContents()).isEqualTo("Answers Contents1")
        );
    }

    @DisplayName("Answer에 A1,A2 를 저장 후 QuestionId 와 Deleted가 False 인 데이터 조회")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        answers.save(AnswerTest.A1);
        answers.save(AnswerTest.A2);
        Answer actual = answers.findByWriterAndDeletedFalse(UserTest.SANJIGI);
        assertThat(actual.getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
    }

    @DisplayName("Answer 에 A1을 저장 후 UserTest.SANJIGI 로 업데이트 테스트")
    @Test
    void update() {
        Answer actual = answers.save(AnswerTest.A1);
        assertThat(actual.getWriterId()).isNotNull();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();

        actual.setWriter(UserTest.SANJIGI);
        Answer expected = answers.findByWriter(UserTest.SANJIGI);
        assertThat(expected.getUpdateAt()).isNotNull();
        assertThat(expected.getWriterId()).isNotNull();
        assertThat(expected.isOwner(UserTest.SANJIGI)).isTrue();
    }

    @DisplayName("Answer 에 A1을 저장 후 다시 delete")
    @Test
    void delete() {
        Answer actual = answers.save(AnswerTest.A1);
        assertThat(actual.getWriterId()).isNotNull();
        assertThat(actual.isOwner(UserTest.JAVAJIGI)).isTrue();

        Answer expectedTrue = answers.findByWriterAndDeletedFalse(UserTest.JAVAJIGI);
        assertThat(expectedTrue).isNotNull();

        answers.delete(actual);
        Answer expectedFalse = answers.findByWriterAndDeletedFalse(UserTest.JAVAJIGI);
        assertThat(expectedFalse).isNull();
    }
}
