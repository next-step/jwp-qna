package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class QuestionTest {
    @Autowired
    private QuestionRepository questionRepository;

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("저장한_객체와_저장된_객체_비교")
    @Test
    void 저장한_객체와_저장된_객체_비교() {
        Question question = new Question("gt", false, "gt");

        Question actual = questionRepository.save(question);
        assertThat(actual).isEqualTo(question);
    }

    @DisplayName("not null 컬럼에 null을 저장")
    @Test
    void notNull_컬럼에_null을_저장() {
        Question question = new Question("gt", false, null);

        assertThatThrownBy(()-> questionRepository.save(question)).isInstanceOf(Exception.class);
    }

    @DisplayName("update 테스트(변경감지)")
    @Test
    void update() {
        Question question = new Question("gt", false, "cheers");
        Question actual = questionRepository.save(question);

        question.setTitle("cheer up!!!!");
        assertThat(actual.getTitle()).isEqualTo("cheer up!!!!");
    }

    @DisplayName("다른 사람이 쓴 질문을 삭제하는 경우")
    @Test
    public void delete_다른_사람이_쓴_글() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);

        assertThatThrownBy(() -> Q2.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
