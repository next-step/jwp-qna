package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AnswerRepository answerRepository;

    Question question;

    @BeforeEach
    void init() {
        question = TestQuestionFactory.create();
    }

    @Test
    void 저장() {
        // when
        Question savedQuestion = save(question);

        // then
        assertThat(savedQuestion.getId()).isNotNull();
    }

    @Test
    void 검색() {
        // given
        Question savedQuestion = save(question);

        // when
        Question foundQuestion = questionRepository.findById(question.getId()).get();

        // then
        assertThat(savedQuestion).isEqualTo(foundQuestion);
    }

    @Test
    void cascadeTest() {
        // given
        Answer answer = TestAnswerFactory.create();
        question.addAnswer(answer);
        save(question);

        // when
        questionRepository.delete(question);

        // then
        assertThat(answerRepository.findById(answer.getId()).isPresent()).isFalse();
    }

    @Test
    void 삭제() {
        // given
        save(question);

        // when
        questionRepository.delete(question);

        // then
        assertThat(questionRepository.findById(question.getId())).isEmpty();
    }

    @Test
    void 질문삭제() {
        // given
        User user = question.getWriter();
        Answer answer = new Answer(user, question, "Answers Contents1");
        userRepository.save(user);
        question.addAnswer(answer);
        save(question);

        // when
        List<DeleteHistory> deleteHistories = question.delete(user).getValue();

        // then
        assertAll(
                () -> assertThat(question.isDeleted()).isTrue(),

                () -> assertThat(deleteHistories.stream()
                        .filter(deleteHistory -> deleteHistory.getContentId().equals(question.getId()))
                        .findAny()
                        .get()
                ).isNotNull(),

                () -> assertThat(deleteHistories.stream()
                        .filter(deleteHistory -> deleteHistory.getContentId().equals(answer.getId()))
                        .findAny()
                        .get()
                ).isNotNull()
        );
    }

    @Test
    void 질문삭제_작성자_다를경우() {
        // given
        User user = question.getWriter();
        User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(user);
        userRepository.save(user2);

        // when, then
        assertThatThrownBy(() ->
                question.delete(user2)).isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문자_답변자_다를경우() {
        // given
        User user = question.getWriter();
        User user2 = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        Answer answer = new Answer(user2, question, "Answers Contents1");
        userRepository.save(user);
        userRepository.save(user2);
        question.addAnswer(answer);
        save(question);

        // when, then
        assertThatThrownBy(() ->
                question.delete(user)).isInstanceOf(CannotDeleteException.class);
    }

    private Question save(Question question) {
        return questionRepository.save(question);
    }
}
