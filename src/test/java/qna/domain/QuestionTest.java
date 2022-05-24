package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByDeletedFalse() {
        final User JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

        final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        List<Question> actual = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual).contains(Q1, Q2)
        );
    }

    @Test
    @DisplayName("질문을 하나 가져올 때, 이 질문에 대한 답변을 같이 가져올 수 있는지 확인한다")
    void findByIdDeletedFalse() {
        final User JAVAJIGI = new User(null, "javajigi", "password", "name", "javajigi@slipp.net");
        final User SANJIGI = new User(null, "sanjigi", "password", "name", "sanjigi@slipp.net");

        final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
        final Question Q2 = new Question("title2", "contents2").writeBy(SANJIGI);

        final Answer A1 = new Answer(JAVAJIGI, Q1, "Answers Contents1");
        final Answer A2 = new Answer(SANJIGI, Q1, "Answers Contents2");

        Q1.addAnswer(A1);
        Q1.addAnswer(A2);

        userRepository.save(JAVAJIGI);
        userRepository.save(SANJIGI);
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        answerRepository.save(A1);
        answerRepository.save(A2);

        Question actual = questionRepository.findByIdAndDeletedFalse(Q1.getId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getAnswers()).contains(A1, A2)
        );
    }
}
