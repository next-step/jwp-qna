package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.DomainTestFactory.*;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("작성자가 일치하는지 테스트")
    public void isOwnerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion().writeBy(user);
        assertThat(question.isOwner(user)).isTrue();
    }

    @Test
    @DisplayName("질문글에 추가한 답변의 질문글 값이 질문글과 동일 객체인지 테스트")
    public void addAnswerTest() {
        User user = createUser("DEVELOPYO");
        Question question = createQuestion();
        Answer answer = createAnswer(user, question);
        question.addAnswer(answer);

        assertThat(answer.getQuestion()).isEqualTo(question);
    }

}
