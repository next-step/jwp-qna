package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class AnswerRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    User testWriter;
    Question testQuestion;
    Answer answer1, answer2;

    @BeforeEach
    void setup() {
        testWriter = userRepository.save(UserTest.ROCKPRO87);
        testQuestion = questionRepository.save(new Question("질문 제목", "질문 내용").writeBy(testWriter));
        answer1 = answerRepository.save(new Answer(testWriter, testQuestion, "답변 내용 1번"));
        answer2 = answerRepository.save(new Answer(testWriter, testQuestion, "답변 내용 2번"));
        testQuestion.addAnswer(answer1);
        testQuestion.addAnswer(answer2);
    }

    @AfterEach
    void clean() {
        answerRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("답변이 생성되고 연관관계가 맺어졌는지 검증")
    void saveWithRelation() {
        Answer expected = new Answer(testWriter, testQuestion, "답변 내용이에요.");
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.getWriter()).isEqualTo(testWriter),
                () -> assertThat(actual.getQuestion()).isEqualTo(testQuestion)
        );
    }

    @Test
    @DisplayName("답변 조회시 연관관계 데이터가 정상적으로 조회되는지 검증")
    void findValidRelation() {
        Answer actual = answerRepository.findByIdAndDeletedFalse(answer1.getId()).orElseThrow(NotFoundException::new);
        assertAll(
                () -> assertThat(actual.getWriter()).isEqualTo(testWriter),
                () -> assertThat(actual.getQuestion()).isEqualTo(testQuestion)
        );
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        assertThat(actual.orElseThrow(NotFoundException::new)).isEqualTo(answer1);
    }
}
