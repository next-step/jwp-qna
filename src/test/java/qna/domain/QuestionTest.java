package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("질문 데이터")
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    static Stream<Arguments> example() {
        return Stream.of(Arguments.of(Q1), Arguments.of(Q2));
    }

    @ParameterizedTest
    @DisplayName("저장")
    @MethodSource("example")
    void save(Question question) {
        //given, when
        Question actual = questionRepository.save(question);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getCreatedAt()).isNotNull(),
            () -> assertThat(actual.getTitle()).isEqualTo(question.getTitle()),
            () -> assertThat(actual.getContents()).isEqualTo(question.getContents()),
            () -> assertThat(actual.getWriterId()).isEqualTo(question.getWriterId())
        );
    }

    @ParameterizedTest
    @DisplayName("아이디로 검색")
    @MethodSource("example")
    void findById(Question question) {
        //given
        Question expected = questionRepository.save(question);

        //when
        Question actual = questionById(expected.getId());

        //then
        assertThat(actual)
            .isEqualTo(expected);
    }

    private Question questionById(Long id) {
        return questionRepository.findByIdAndDeletedFalse(id)
            .orElseThrow(() -> new EntityNotFoundException(String.format("id(%s) is not found", id)));
    }
}
