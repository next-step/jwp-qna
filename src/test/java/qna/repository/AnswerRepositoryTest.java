package qna.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    Answer answer1;
    Answer answer2;
    Question question1;
    Question question2;
    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder("javajigi", "password", "name")
                .email("javajigi@slipp.net")
                .build();
        user2 = User.builder("sanjigi", "password", "name")
                .email("sanjigi@slipp.net")
                .build();
        question1 = Question.builder("title1")
                .contents("contents1")
                .build()
                .writeBy(user1);
        question2 = Question.builder("title2")
                .contents("contents2")
                .build()
                .writeBy(user2);
        answer1 = Answer.builder(user1, question1)
                .contents("Answers Contents1")
                .build();
        answer2 = Answer.builder(user2, question2)
                .contents("Answers Contents2")
                .build();
    }

    @DisplayName("저장 테스트")
    @Test
    void save() {
        Answer answer = answerRepository.save(answer1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriter()).isEqualTo(answer1.getWriter()),
                () -> assertThat(answer.getContents()).isEqualTo(answer1.getContents())
        );
    }

    @DisplayName("질문 ID 로 조회 테스트")
    @Test
    void findByQuestionIdAndDeletedFalse() {
        answerRepository.save(answer1);
        answerRepository.save(answer2);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
        assertThat(answers).hasSize(1);
    }

    @DisplayName("delete 가 잘 되었는지 테스트")
    @Test
    void delete() {
        answerRepository.save(answer1);
        Answer answer = answerRepository.save(answer2);
        answerRepository.delete(answer);
        List<Answer> answers = answerRepository.findByQuestionIdAndDeletedFalse(question1.getId());
        assertThat(answers).hasSize(1);
    }

    @DisplayName("ID로 조회 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Answer answer = answerRepository.save(answer1);
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual.orElse(null)).isEqualTo(answer);
    }

    @DisplayName("Writer 가 없을 때 UnAuthorizedException 발생 테스트")
    @Test
    void unAuthorizedException() {
        assertThatThrownBy(() -> Answer.builder(null, question1)
                .contents("Answers Contents1")
                .build())
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessageContaining("작성자 정보가 없습니다.");
    }

    @DisplayName("Question 이 없을 때 UnAuthorizedException 발생 테스트")
    @Test
    void notFoundException() {
        assertThatThrownBy(() -> Answer.builder(user1, null)
                .contents("Answers Contents1")
                .build())
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("질문 정보가 없습니다.");
    }
}
