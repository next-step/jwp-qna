package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.generator.UserGenerator.generateQuestionWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Domain:Question")
public class QuestionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 생성")
    public void createQuestionEntityTest() {
        // Given
        final String contents = "질문 내용";
        final String title = "질문 제목";

        // When
        Question given = new Question(title, contents).writeBy(generateQuestionWriter());

        // Then
        assertAll(
            () -> assertThat(given).isEqualTo(new Question(title, contents).writeBy(generateQuestionWriter())),
            () -> assertThat(given.getTitle()).isEqualTo(title),
            () -> assertThat(given.getContents()).isEqualTo(contents)
        );
    }
}
