package qna.domain.question;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import qna.domain.user.UserId;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
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
        assertThat(findQuestion).hasValueSatisfying(question -> assertThat(question)
                .extracting("title", "contents", "writer.userId")
                .contains(new Title("question1"), new Contents("contents1"), new UserId("javajigi"))
        );
    }
}

