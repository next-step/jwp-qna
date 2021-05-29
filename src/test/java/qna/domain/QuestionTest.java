package qna.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.exceptions.EmptyStringException;
import qna.exceptions.NullStringException;
import qna.exceptions.StringTooLongException;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private static final String TEXT_LENGTH_111 = "Length = 111. This value is too long for the column of this entity. Could not execute the insert SQL statement.";

    @DisplayName("글쓴이 확인")
    @Test
    void isOwner() {
        assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
        assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse();
    }

    @DisplayName("Null 문자열 저장 불가")
    @Test
    void create_NullString_ExceptionThrown() {
        assertThatExceptionOfType(NullStringException.class).isThrownBy(() ->
            new Question(null, "contents")
        );
    }

    @DisplayName("빈 문자열 저장 불가")
    @Test
    void create_EmptyString_ExceptionThrown() {
        assertThatExceptionOfType(EmptyStringException.class).isThrownBy(() ->
            new Question("", "contents")
        );
    }

    @DisplayName("문자열 길이 제한")
    @Test
    void create_StringTooLong_ExceptionThrown() {
        assertThatExceptionOfType(StringTooLongException.class).isThrownBy(() ->
            new Question(TEXT_LENGTH_111, "contents")
        );
    }
}
