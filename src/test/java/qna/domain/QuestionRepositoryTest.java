package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Example;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class QuestionRepositoryTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    public static final Question Q3 = new Question("title3", "contents1").writeBy(UserTest.HOONHEE);

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeAll
    void setup() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);
        questionRepository.save(Q3);
    }

    @Test
    @DisplayName("저장")
    void save() {
        Question expected = new Question("질문입니다.", "내용입니다.").writeBy(UserTest.HOONHEE);
        Question actual = questionRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    @Test
    @DisplayName("단건조회 (getOne) 테스트")
    void getOne() {
        Question expected = Q1;
        Question actual = questionRepository.getOne(expected.getId());
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(expected.getId())
        );
    }

    @Test
    @DisplayName("단건조회시 (getOne) 데이터 없을경우 참조(lazyLoad)하는 시점에 예외발생")
    void getOne_null_lazy() {
        Question question = questionRepository.getOne(123L);

        assertThatThrownBy(() -> System.out.println(question))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("단건조회 (findOne)")
    void findOne() {
        Question expected = new Question();
        expected.setTitle("title1");
        expected.setContents("contents1");
        Question actual = questionRepository.findOne(Example.of(expected)).get();
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(expected.getContents())
        );
    }

    @Test
    @DisplayName("단건조회 (findOne) 여러건 조회될경우 예외발생")
    void findOne_moreThanOneRow() {
        Question expected = new Question();
        expected.setContents("contents1");

        assertThatThrownBy(() -> questionRepository.findOne(Example.of(expected)))
            .isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    @Test
    @DisplayName("단건조회 (findById)")
    void findById() {
        Question expected = Q1;
        Question actual = questionRepository.findById(Q1.getId()).get();
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getWriterId()).isEqualTo(expected.getId())
        );
    }

    @Test
    @DisplayName("전체 리스트 조회 (findAll)")
    void findAll() {
        List<Question> questions = questionRepository.findAll();
        List<Long> ids = questions.stream()
            .mapToLong(Question::getId)
            .boxed()
            .collect(Collectors.toList());

        assertThat(questions).hasSize(3);
        assertThat(ids).contains(Q1.getId(), Q2.getId(), Q3.getId());
    }

    @Test
    @DisplayName("전체 리스트 조회 (삭제여부가 false인것)")
    void findByDeletedFalse() {
        List<Question> questions = questionRepository.findByDeletedFalse();
        List<Long> ids = questions.stream()
            .mapToLong(Question::getId)
            .boxed()
            .collect(Collectors.toList());

        assertThat(questions).hasSize(3);
        assertThat(ids).contains(Q1.getId(), Q2.getId(), Q3.getId());
    }

    @Test
    @DisplayName("리스트 조회 (findBy)")
    void findBy() {
        List<Question> questions = questionRepository.findByTitleAndContents("title1", "contents1");
        List<Long> ids = questions.stream()
            .mapToLong(Question::getId)
            .boxed()
            .collect(Collectors.toList());

        assertThat(questions).hasSize(1);
        assertThat(ids).contains(Q1.getId());
    }

    @Test
    @DisplayName("카운트")
    void count() {
        long count = questionRepository.count(Example.of(Q1));
        assertThat(count).isEqualTo(1);
    }

    @Test
    @DisplayName("카운트 (countBy)")
    void countBy() {
        long count = questionRepository.countByContents("contents1");
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("수정")
    void update() {
        Question expected = questionRepository.findById(Q1.getId()).get();
        expected.setTitle("제목 변경");

        Question actual = questionRepository.findById(expected.getId()).get();
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(expected.getTitle())
        );
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        questionRepository.delete(Q1);
        questionRepository.flush();

        assertThat(questionRepository.findById(Q1.getId()).isPresent()).isFalse();
    }

}
