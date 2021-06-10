package qna.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import qna.domain.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static qna.domain.ContentType.QUESTION;

@SpringBootTest
@DisplayName("DeleteHistory 테스트")
class DeleteHistoryServiceTest {

    @Autowired
    private DeleteHistoryService deleteHistoryService;

    @Autowired
    private DeleteHistoryRepository deleteHistories;

    @Autowired
    private UserRepository users;
    @Autowired
    private QuestionRepository questions;

    private User javajigi;
    private Question question1;
    private Question question2;
    private Question question3;

    @BeforeEach
    void setUp() {
        javajigi = new User(new UserId("javajigi"), new Password("password"), new Name("name"), new Email("javajigi@slipp.net"));
        users.save(javajigi);

        question1 = new Question(new Title("title1"), new Contents("contents1")).writeBy(javajigi);
        question2 = new Question(new Title("title2"), new Contents("contents2")).writeBy(javajigi);
        question3 = new Question(new Title("title3"), new Contents("contents3")).writeBy(javajigi);
        questions.saveAll(asList(question1, question2, question3));
    }

    @Test
    @DisplayName("saveAll_성공")
    void saveAll_성공() {
        // Given
        List<DeleteHistory> savedDeleteHistories = new ArrayList<DeleteHistory>();
        savedDeleteHistories.add(new DeleteHistory(QUESTION, question1.getId(), javajigi));
        savedDeleteHistories.add(new DeleteHistory(QUESTION, question2.getId(), javajigi));
        savedDeleteHistories.add(new DeleteHistory(QUESTION, question3.getId(), javajigi));

        // When
        deleteHistoryService.saveAll(savedDeleteHistories);
        List<DeleteHistory> foundDeleteHistories = deleteHistories.findAll();

        // Then
        for (int i = 0; i < foundDeleteHistories.size(); i++) {
            assertThat(foundDeleteHistories.get(i).getId()).isEqualTo(savedDeleteHistories.get(i).getId());
            assertThat(foundDeleteHistories.get(i).isQuestionType()).isTrue();
            assertThat(foundDeleteHistories.get(i).isWriter(javajigi)).isTrue();
        }
    }

}