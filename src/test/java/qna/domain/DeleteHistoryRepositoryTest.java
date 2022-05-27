package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class DeleteHistoryRepositoryTest {
    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    private Question question;
    private Answer answer;
    private DeleteHistory answerHistory, questionHistory;

    @BeforeEach
    void init() {
        //given
        User writer = userRepository.save(new User("javajigi", "password", "name", "javajigi@slipp.net"));
        question = questionRepository.save(new Question("title1", "contents1").writeBy(writer));
        answer = answerRepository.save(new Answer(writer, question, "Answers Contents1"));

        questionHistory = new DeleteHistory(ContentType.QUESTION, question.getWriter().getId(), question.getWriter(), LocalDateTime.now());
        answerHistory = new DeleteHistory(ContentType.ANSWER, answer.getWriter().getId(), answer.getWriter(), LocalDateTime.now());
    }

    @Test
    @DisplayName("질문 삭제 히스토리 저장 및 값 비교 테스트")
    void save_by_question() {
        //when
        DeleteHistory actual = deleteHistoryRepository.save(questionHistory);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(questionHistory)
        );
    }

    @Test
    @DisplayName("답변 삭제 히스토리 저장 및 값 비교 테스트")
    void save_by_answer() {
        //when
        DeleteHistory actual = deleteHistoryRepository.save(answerHistory);

        //then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isEqualTo(answerHistory)
        );
    }

    @Test
    @DisplayName("질문 작성자 id로 삭제기록 목록 조회")
    void findById_answer_writer() {
        //when
        DeleteHistory expected = deleteHistoryRepository.save(questionHistory);

        //when
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(question.getWriter().getId());

        //then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }

    @Test
    @DisplayName("답변 작성자 id로 삭제기록 목록 조회")
    void findById_question_writer() {
        //when
        DeleteHistory expected = deleteHistoryRepository.save(answerHistory);

        //when
        Optional<DeleteHistory> actual = deleteHistoryRepository.findById(answer.getWriter().getId());

        //then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get()).isSameAs(expected)
        );
    }
}
