package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    private Question question1;
    private Question question2;

    @BeforeEach
    void saveDefaultQuestions() {
        question1 = questionRepository.save(Q1);
        question2 = questionRepository.save(Q2);
    }

    @Test
    void save() {
        assertAll(
                () -> assertThat(question1).isNotNull(),
                () -> assertThat(question1.getTitle()).isEqualTo(Q1.getTitle())
        );
    }

    @Test
    @DisplayName("질문 수정 : 질문 제목, 질문 내용")
    void update() {
        question1.setTitle("첫번째 질문");
        question1.setContents("첫번째 질문 내용입니다.");
        Question actual = questionRepository.save(question1);
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getTitle()).isEqualTo("첫번째 질문"),
                () -> assertThat(actual.getContents()).isEqualTo("첫번째 질문 내용입니다.")
        );
    }

    @Test
    @DisplayName("전체 질문 목록 수 조회")
    void countQuestions() {
        Long count = questionRepository.count();
        assertThat(count).isEqualTo(2L);
    }

    @ParameterizedTest
    @CsvSource(value = {"0:title1", "1:title2"}, delimiter = ':')
    @DisplayName("전체 질문 목록 조회")
    void getQuestions(int index, String title) {
        String orderByColumn = "id";
        List<Question> questions = questionRepository.findAll(Sort.by(Sort.Direction.ASC, orderByColumn));
        assertAll(
                () -> assertThat(questions.size()).isEqualTo(2L),
                () -> assertThat(questions.get(index).getTitle()).isEqualTo(title)
        );
    }

    @Test
    @DisplayName("작성자 ID를 통해 질문 목록 수 가져오기")
    void countByWriter() {
        assertThat(questionRepository.countByWriter(UserTest.JAVAJIGI)).isEqualTo(1L);
    }

    @Test
    @DisplayName("작성자 ID를 통해 질문 목록 가져오기")
    void getQuestionsByWriter() {
        List<Question> questions = questionRepository.findByWriter(UserTest.JAVAJIGI);
        assertAll(
                () -> assertThat(questions.size()).isEqualTo(1L),
                () -> assertThat(questions.get(0).getTitle()).isEqualTo("title1")
        );
    }

}
