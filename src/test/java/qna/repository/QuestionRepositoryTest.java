package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import qna.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.QUESTION_1;
import static qna.domain.QuestionTest.QUESTION_2;

@DisplayName("질문 Repository")
class QuestionRepositoryTest extends RepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("저장_성공")
    @Test
    void save() {

        Question question = questionRepository.save(new Question("title1", "contents1").writeBy(javajigi));

        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getContents()).isEqualTo(QUESTION_1.getContents()),
                () -> assertThat(question.getTitle()).isEqualTo(QUESTION_1.getTitle()),
                () -> assertThat(question.isDeleted()).isFalse(),
                () -> assertThat(question.getWriter()).isNotNull(),
                () -> assertThat(question.getCreatedAt()).isNotNull(),
                () -> assertThat(question.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("findByDeletedFalse_조회_성공")
    @Test
    void findByDeletedFalse() {

        List<Question> questions = questionRepository.findByDeletedFalse();

        assertAll(
                () -> assertThat(questions).hasSize(2),
                () -> assertThat(questions).containsExactly(question1, question2)
        );
    }

    @DisplayName("findByIdAndDeletedFalse_조회_성공")
    @Test
    void findByIdAndDeletedFalse() {
        assertAll(
                () -> assertThat(question2.getId()).isNotNull(),
                () -> assertThat(question2.getContents()).isEqualTo(QUESTION_2.getContents()),
                () -> assertThat(question2.getTitle()).isEqualTo(QUESTION_2.getTitle()),
                () -> assertThat(question2.isDeleted()).isFalse(),
                () -> assertThat(question2.getWriter()).isNotNull(),
                () -> assertThat(question2.getCreatedAt()).isNotNull(),
                () -> assertThat(question2.getUpdatedAt()).isNotNull()
        );
    }

    @DisplayName("삭제_성공")
    @Test
    void delete() {

        assertThat(question2).isNotNull();

        question2.setDeleted(true);

        questionRepository.save(question2);

        assertThat(questionRepository.findByIdAndDeletedFalse(question2.getId())).isNotPresent();
    }
}
