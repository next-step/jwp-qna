package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;

class QuestionRepositoryTest extends BaseDataJpaTest {

    @Autowired
    private QuestionRepository repository;

    Question question;
    Question savedQuestion;

    @BeforeEach
    void setUp() {
        question = new Question(Q1.getTitle(), Q1.getContents());
        savedQuestion = repository.save(question);
    }

    @Test
    @DisplayName("서로 같은 데이터를 가진 엔티티는 동일해야 한다")
    void entitySameAsTest() {
        assertThat(savedQuestion).isSameAs(question);
    }

    @Test
    @DisplayName("조회한 데이터와 같은 id 값을 가진 엔티티는 동일해야 한다")
    void entityRetrieveTest() {
        Question findQuestion1 = repository.findByIdAndDeletedFalse(question.getId()).get();
        Question findQuestion2 = repository.findById(question.getId()).get();

        assertAll(
                () -> assertThat(findQuestion1).isSameAs(savedQuestion),
                () -> assertThat(findQuestion2).isSameAs(savedQuestion),
                () -> assertThat(findQuestion1).isSameAs(findQuestion2)
        );
    }

    @Test
    @DisplayName("저장 전 후의 데이터가 같아야 한다")
    void entitySameValueTest() {
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getWriterId()).isEqualTo(question.getWriterId()),
                () -> assertThat(savedQuestion.isDeleted()).isEqualTo(question.isDeleted())
        );
    }

    @Test
    @DisplayName("inert 시 createAt 이 자동으로 입력된다.")
    void dateAutoCreateTest() {

        assertAll(
                () -> assertThat(savedQuestion.getCreateAt()).isNotNull(),
                () -> assertThat(savedQuestion.getUpdateAt()).isNull()
        );
    }

    @Test
    @DisplayName("update 시 updateAt 이 자동으로 변경된다.")
    void dateAutoModifyTest() {
        savedQuestion.setContents("update date?");
        repository.flush();

        assertThat(savedQuestion.getUpdateAt()).isNotNull();
    }

    @AfterEach
    void endUp() {
        repository.deleteAll();
    }

}