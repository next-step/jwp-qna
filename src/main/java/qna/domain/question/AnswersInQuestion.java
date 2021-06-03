package qna.domain.question;

import qna.CannotDeleteException;
import qna.domain.QnAExceptionMessage;
import qna.domain.answer.Answer;
import qna.domain.history.DeleteHistory;
import qna.domain.history.DeleteHistorys;
import qna.domain.user.User;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class AnswersInQuestion {

	@OneToMany(mappedBy = "question")
	private List<Answer> answers = new ArrayList<>();

	public boolean add(Answer answer) {
		if(this.answers.contains(answer)) {
			return false;
		}

		return this.answers.add(answer);
	}

	public void checkOwner(final User loginUser) throws CannotDeleteException {
		boolean isHaveNoOwner = answers.stream().anyMatch(answer -> answer.isOwner(loginUser) == false);
		if (isHaveNoOwner) {
			throw new CannotDeleteException(QnAExceptionMessage.NO_AUTH_ANSWER);
		}
	}

	public DeleteHistorys deleteAll() {
		List<DeleteHistory> deleteHistories = answers.stream()
													 .map(answer -> answer.delete())
													 .collect(Collectors.toList());
		return DeleteHistorys.of(deleteHistories);
	}
}
