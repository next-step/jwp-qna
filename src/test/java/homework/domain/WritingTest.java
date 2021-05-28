package homework.domain;

import homework.domain.entity.Answer;
import homework.domain.entity.Question;
import homework.domain.entity.User;
import homework.domain.repository.AnswerRepository;
import homework.domain.repository.QuestionRepository;
import homework.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class WritingTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Test
    @DisplayName("사용자 생성")
    void createUser() {
        //When
        User expected = User.builder()
                .userId("hun")
                .name("훈")
                .email("hun@naver.com")
                .password("1234")
                .build();

        User actual = userRepository.save(expected);

        //Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    @DisplayName("질문 생성")
    void createQuestion() {
        //Given
        User hun = User.builder()
                .userId("hun")
                .name("훈")
                .email("hun@naver.com")
                .password("1234")
                .build();

        //When
        Question expected = Question.builder()
                .user(hun)
                .title("제목")
                .contents("내용입니다.")
                .build();

        Question actual = questionRepository.save(expected);

        //Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUser()).isEqualTo(hun)
        );
    }

    @Test
    @DisplayName("답변 생성")
    void createAnswer() {
        //Given
        User hun = User.builder()
                .userId("hun")
                .name("훈")
                .email("hun@naver.com")
                .password("1234")
                .build();

        User hong = User.builder()
                .userId("hong")
                .name("홍")
                .email("hong@naver.com")
                .password("1234")
                .build();

        Question question = Question.builder()
                .user(hun)
                .title("제목")
                .contents("내용입니다.")
                .build();

        //When
        Answer expected = Answer.builder()
                .contents("답변입니다.")
                .question(question)
                .user(hong)
                .build();

        Answer actual = answerRepository.save(expected);

        //Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getUser()).isEqualTo(hong),
                () -> assertThat(actual.getQuestion()).isEqualTo(question),
                () -> assertThat(actual.getQuestion().getUser()).isEqualTo(hun)
        );
    }

    @Test
    @DisplayName("질문 삭제")
    void deleteAnswer() {
        //TODO
    }

}