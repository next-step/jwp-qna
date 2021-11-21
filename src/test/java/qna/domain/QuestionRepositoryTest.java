package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.NotFoundException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("DB에 저장된 질문을 저장한 후 해당 질문의 ID로 조회할 수 있다.")
    void save_질문생성() {
        final User user = 사용자_저장("questionWriter");

        final Question actual = 질문_저장(user);

        final Question expected = 저장된_질문_조회(actual);

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
    @DisplayName("질문을 저장한 후 해당 ID와 삭제되지 않은 조건을 이용하여 질문을 조회할 수 있다.")
    void findByIdAndDeletedFalse_조회() {
        final User user = 사용자_저장("questionWriter");

        final Question actual = 질문_저장(user);

        final Question expected = 저장된_질문_삭제조건_조회(actual);

        assertAll(
                () -> assertThat(actual).isEqualTo(expected),
                () -> assertThat(actual.isDeleted()).isEqualTo(expected.isDeleted())
        );
    }

    @Test
    @DisplayName("질문을 저장한 후 삭제 처리를 한 뒤 해당 ID와 삭제 조건을 이용하여 질문을 조회할 수 있다.")
    void findByIdAndDeletedFalse_삭제_조회() {
        final User user = 사용자_저장("questionWriter");

        final Question actual = 질문_저장(user);

        actual.deleteQuestion(actual.getWriter(), LocalDateTime.now());

        assertThatThrownBy(() -> questionRepository.findByIdAndDeletedFalse(actual.getId())
                .orElseThrow(NotFoundException::new))
                .isInstanceOf(NotFoundException.class);
    }


    private User 사용자_저장(String writer) {
        return userRepository.save(new User(writer, "password", "lsh", "lsh@mail.com"));
    }

    private Question 질문_저장(User user) {
        return questionRepository.save(new Question("title", "contents", user));
    }

    private Question 저장된_질문_조회(Question actual) {
        return questionRepository.findById(actual.getId()).orElseThrow(NotFoundException::new);
    }

    private Question 저장된_질문_삭제조건_조회(Question actual) {
        return questionRepository.findByIdAndDeletedFalse(actual.getId()).orElseThrow(NotFoundException::new);
    }
}
