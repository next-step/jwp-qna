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
            () -> assertThat(given.getId()).as("IDENTITY 전략에 의해 DB에서 생성되는 PK값의 Null 여부").isNull(),
            () -> assertThat(given.getTitle()).isEqualTo(title),
            () -> assertThat(given.getContents()).isEqualTo(contents),
            () -> assertThat(given.getCreatedAt()).as("객체 생성일시 정보의 할당 여부").isNotNull(),
            () -> assertThat(given.getUpdatedAt()).as("객체 수정 시 할당되는 수정일시 정보의 Null 여부").isNull()
        );
    }
}
