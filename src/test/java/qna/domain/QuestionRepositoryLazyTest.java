package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.proxy.HibernateProxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryLazyTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("Writer가 정상적으로 지연로딩하는지 확인한다")
    void writerAndQuestionMustLoadedLazyFetchOnAnswerEntity() {
        // given
        Long questionId = 10000L;

        // when
        Question actual = questionRepository.findByIdAndDeletedFalse(questionId).orElse(null);


        // then
        assertThat(actual.getWriter() instanceof HibernateProxy).isTrue();
    }
}
