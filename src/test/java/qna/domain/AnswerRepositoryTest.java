package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    private Question question1;
    private Question question2;
    private User answerWriter;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();
        answerRepository.deleteAll();
        userRepository.deleteAll();
        User questionWriter = userRepository.save(new User("test1234", "1234", "테스트", "test1234@gmail.com"));
        question1 = questionRepository.save(new Question("title1", "contents1").writeBy(questionWriter));
        question2 = questionRepository.save(new Question("title2", "contents2").writeBy(questionWriter));
        answerWriter = userRepository.save(new User("test5678", "5678", "테스트", "test5678@gmail.com"));
    }
    @DisplayName("답장을 저장한다.")
    @Test
    void save() {
        Answer answer =  new Answer(answerWriter, question1, "Answers Contents1");
        final Answer savedAnswer = answerRepository.save(answer);
        assertThat(savedAnswer.getId()).isNotNull();

        assertAll(
            () -> assertThat(savedAnswer.getId()).isNotNull(),
            () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
            () -> assertThat(savedAnswer.getWriteBy()).isEqualTo(answer.getWriteBy()),
            () -> assertThat(savedAnswer.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("저장한 엔티티와 동일한 id로 조회한 엔티티는 동일성 보장한다.")
    @Test
    void sameEntity() {
        final Answer saved = answerRepository.save(new Answer(answerWriter, question1, "Answers Contents1"));
        final Answer answer = answerRepository.findById(saved.getId()).get();
        assertThat(answer.getId()).isEqualTo(saved.getId());
        assertThat(answer).isEqualTo(saved);
    }

    @DisplayName("Answer에 대한 Question을 변경한다.")
    @Test
    void toQuestion() {
        final Answer answer = new Answer(answerWriter, question1, "test");
        answer.setQuestion(question2);
        final Answer saved = answerRepository.save(answer);
        assertThat(saved.getQuestion()).isEqualTo(question2);
    }
}
