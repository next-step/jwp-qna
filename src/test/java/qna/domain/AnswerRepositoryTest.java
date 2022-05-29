package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
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
    void setup(){
        testWriter = userRepository.save(UserTest.ROCKPRO87);
        testQuestion = questionRepository.save(new Question("질문 제목", "질문 내용").writeBy(testWriter));
        answer1 = answerRepository.save(new Answer(testWriter, testQuestion, "답변 내용 1번"));
        answer2 = answerRepository.save(new Answer(testWriter, testQuestion, "답변 내용 2번"));
    }

    @AfterEach
    void clean(){
        userRepository.deleteAll();
        questionRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    @DisplayName("답변 저장")
    void save() {
        Answer expected = new Answer(testWriter, testQuestion, "답변 내용이에요.");
        Answer actual = answerRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    void findByDeletedFalse() {
        List<Answer> actual = answerRepository.findByQuestionAndDeletedFalse(testQuestion);
        answerRepository.flush();
        assertThat(actual).contains(answer1, answer2);
    }

    @Test
    void findByIdAndDeletedFalse() {
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer1.getId());
        answerRepository.flush();
        assertThat(actual.get()).isEqualTo(answer1);
    }
}
