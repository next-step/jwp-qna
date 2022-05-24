package qna.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static qna.domain.UserTest.JAVAJIGI;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import qna.domain.Answer;
import qna.domain.DeleteHistory;
import qna.domain.DeleteHistoryContent;
import qna.domain.DeleteHistoryRepository;
import qna.domain.Question;
import qna.domain.QuestionRepository;

@ExtendWith(MockitoExtension.class)
class DeleteHistoryServiceTest {
    @Mock
    private DeleteHistoryRepository deleteHistoryRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private DeleteHistoryService deleteHistoryService;

    private Question question;
    private Answer answer;
    private List<DeleteHistory> deleteHistories;

    @BeforeEach
    void setUp() {
        question = new Question(1L, "title1", "contents1").writeBy(JAVAJIGI);
        answer = new Answer(1L, JAVAJIGI, question, "Answers Contents1");
        question.delete(JAVAJIGI);
        question.addAnswer(answer);
    }

    @Test
    void insert_성공() {
        when(questionRepository.findByIdAndDeletedTrue(question.getId())).thenReturn(Optional.of(question));
        deleteHistoryService.insertDeletedQuestionAndLinkedAnswers(question.getId());

        verifyDeleteHistories();
        when(deleteHistoryRepository.findByDeleter(question.getWriter())).thenReturn(deleteHistories);
        assertThat(deleteHistoryService.findDeleteHistoriesByDeleter(question.getWriter())).hasSize(2);
    }

    private void verifyDeleteHistories() {
        deleteHistories = Arrays.asList(
                new DeleteHistory(DeleteHistoryContent.remove(question), question.getWriter()),
                new DeleteHistory(DeleteHistoryContent.remove(answer), answer.getWriter())
        );
        verify(deleteHistoryRepository).saveAll(deleteHistories);
    }
}
