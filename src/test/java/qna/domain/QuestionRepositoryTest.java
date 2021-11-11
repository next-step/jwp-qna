package qna.domain;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = NONE)
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("삭제 되지 않은 질문 목록을 조회한다.")
    void findByDeletedFalse() {
        //when
        List<Question> remainQuestions = questionRepository.findByDeletedFalse();

        //then
        assertThat(remainQuestions).hasSize(2);
    }

    @Test
    @DisplayName("삭제 되지 않은 질문 한 건을 조회한다.")
    void findByIdAndDeletedFalse() {
        //given //when
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(1L);

        //then
        AssertionsForClassTypes.assertThat(findQuestion).hasValueSatisfying(question -> assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo("question1"),
                () -> assertThat(question.getContents()).isEqualTo("contents1"),
                () -> assertThat(question.getWriter().getName()).isEqualTo("user1")
        ));
    }
}

