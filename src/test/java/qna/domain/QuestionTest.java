package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository repository;

    @Test
    @DisplayName("질문 글을 저장한다.")
    void save() {
        //given //when
        Question saved = repository.save(Q1);

        //then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getTitle()).isEqualTo(Q1.getTitle()),
                () -> assertThat(saved.getContents()).isEqualTo(Q1.getContents()),
                () -> assertThat(saved.getWriterId()).isEqualTo(Q1.getWriterId())
        );
    }

    @Test
    @DisplayName("삭제 되지 않은 질문 목록을 조회한다.")
    void findByDeletedFalse() {
        //given
        repository.save(Q1);
        Question saved = repository.save(Q2);

        saved.setDeleted(true);

        //when
        List<Question> remainQuestions = repository.findByDeletedFalse();

        //then
        assertThat(remainQuestions).hasSize(1);
    }

    @Test
    @DisplayName("삭제 되지 않은 질문 한 건을 조회한다.")
    void findByIdAndDeletedFalse() {
        //given
        Question saved = repository.save(Q2);

        //when
        Optional<Question> findQuestion = repository.findByIdAndDeletedFalse(saved.getId());

        //then
        assertThat(findQuestion.isPresent()).isTrue();
        Question question = findQuestion.get();
        assertAll(
                () -> assertThat(question.getId()).isNotNull(),
                () -> assertThat(question.getTitle()).isEqualTo(Q2.getTitle()),
                () -> assertThat(question.getContents()).isEqualTo(Q2.getContents()),
                () -> assertThat(question.getWriterId()).isEqualTo(Q2.getWriterId())
        );
    }
}
