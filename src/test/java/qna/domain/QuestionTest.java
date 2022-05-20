package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.repository.QuestionRepository;
import qna.domain.repository.UserRepository;
import qna.exception.CannotDeleteException;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("등록된 질문을 삭제한다.(작성자가 본인일 경우)")
    @Test
    void delete(){
        long questionId = 1L;
        long writerId = 1L;
        Question question = questionRepository.findById(questionId).get();
        User loginUser = userRepository.findById(writerId).get();

        question.delete(loginUser);
        questionRepository.flush();
        entityManager.clear();

        Question actual = questionRepository.findById(questionId).get();
        assertThat(actual.isDeleted()).isTrue();
    }

    @DisplayName("등록된 질문을 삭제한다.(작성자가 본인이 아닌 경우)")
    @Test
    void delete_not_writer(){
        long questionId = 1L;
        long loginUserId = 2L;
        Question question = questionRepository.findById(questionId).get();
        User loginUser = userRepository.findById(loginUserId).get();

        assertThatThrownBy(()-> question.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class)
                .hasMessageContaining("[ERROR] 작성자가 아닌 경우 삭제할 수 없습니다.");
    }
}
