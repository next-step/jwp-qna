package qna.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("질문 기능 테스트")
@DataJpaTest
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    static void setUp(@Autowired UserRepository userRepository) {
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @DisplayName("질문정보 단일 저장을 테스트합니다.")
    @Test
    public void 단일_저장() {
        Question savedQuestion = questionRepository.save(Q1);

        assertAll(
                () -> assertThat(savedQuestion).isInstanceOf(Question.class).isNotNull(),
                () -> assertThat(savedQuestion.getId()).isNotNull().isGreaterThan(0L)
        );
    }

    @DisplayName("질문정보 목록 저장을 테스트합니다.")
    @Test
    public void 목록_저장() {
        List<Question> questions = new ArrayList<>();
        questions.add(Q1);
        questions.add(Q2);

        List<Question> savedQuestions = questionRepository.saveAll(questions);

        assertAll(
                () -> assertThat(savedQuestions).isNotEmpty().hasSize(questions.size()),
                () -> savedQuestions.forEach(savedQuestion -> {
                    assertThat(savedQuestion).isInstanceOf(Question.class).isNotNull();
                    assertThat(savedQuestion.getId()).isNotNull().isGreaterThan(0L);

                    // questions 목록의 내용이 저장된게 맞는지 더블체크
                    Optional<Question> hasQuestion = questions.stream().filter(question -> question.getId() == savedQuestion.getId()).findAny();
                    assertThat(hasQuestion.isPresent()).isTrue();
                })
        );
    }

    @DisplayName("질문정보 조회 성공을 테스트합니다.")
    @Test
    public void 조회_성공() {
        Question savedQuestion = questionRepository.save(Q1);
        Optional<Question> question = questionRepository.findById(savedQuestion.getId());

        assertAll(
                () -> assertThat(question).isPresent(),
                () -> assertThat(question.get().getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @DisplayName("삭제되지 않은 질문정보 조회 성공을 테스트합니다.")
    @Test
    public void 삭제되지_않은_질문_조회_성공() {
        Question savedQuestion = questionRepository.save(Q1);
        Optional<Question> question = questionRepository.findByIdAndDeletedFalse(savedQuestion.getId());

        assertAll(
                () -> assertThat(question).isPresent(),
                () -> assertThat(question.get().getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @DisplayName("삭제되지 않은 질문 조회를 테스트합니다.")
    @Test
    public void 삭제되지_않은_질문_조회() {
        List<Question> questions = new ArrayList<>();
        questions.add(Q1);
        questions.add(Q2);

        questionRepository.saveAll(questions);

        List<Question> notDeletedQuestions = questionRepository.findByDeletedFalse();
        assertThat(notDeletedQuestions).hasSize(questions.size());
    }

    @DisplayName("질문정보(id) 조회 실패를 테스트합니다.")
    @Test
    public void id_조회_실패() {
        questionRepository.save(Q1);

        Optional<Question> question = questionRepository.findById(0L);
        assertThat(question.isPresent()).isFalse();
    }

}
