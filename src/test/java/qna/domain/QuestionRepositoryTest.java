package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserRepositoryTest.JAVAJIGI;

@DataJpaTest
public class QuestionRepositoryTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserRepositoryTest.SANJIGI);

    private User questionWriter;
    private Question question;

    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @BeforeEach
    void setUp() {
        questionWriter = new User("questionWriter", "password", "lsh", "lsh@mail.com");
        question = new Question("title", "contents");
    }

    @Test
    @DisplayName("DB에 저장된 질문을 저장한 후 해당 질문의 ID로 조회할 수 있다.")
    void save_질문생성() {
        final Question actual = userSave(questionWriter);

        final Question expected = questions.findById(actual.getId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriter()).isSameAs(expected.getWriter()),
                () -> assertThat(actual.getAnswers()).isSameAs(expected.getAnswers()),
                () -> assertThat(actual.getContents()).isEqualTo(expected.getContents()),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("DB에 질문을 저장한 후 해당 ID와 삭제되지 않은 조건을 이용하여 질문을 조회할 수 있다.")
    void findByIdAndDeletedFalse_조회() {
        final Question actual = userSave(questionWriter);

        final Question expected = questions.findByIdAndDeletedFalse(actual.getId()).orElseThrow(NotFoundException::new);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );

    }

    @Test
    @DisplayName("DB에 질문을 저장한 후 삭제 처리를 한 뒤 해당 ID와 삭제 조건을 이용하여 질문을 조회할 수 있다.")
    void findByIdAndDeletedFalse_삭제_조회() {
        final Question actual = userSave(questionWriter);

        actual.setDeleted(true);

        assertThatThrownBy(() -> questions.findByIdAndDeletedFalse(actual.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }

    private Question userSave(User questionWriter) {
        question.writeBy(users.save(questionWriter));
        return questions.save(question);
    }
}
