package qna.domain.answer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import qna.CannotDeleteException;
import qna.domain.deletehistory.DeleteHistories;
import qna.domain.user.User;

public class Answers {

	private final List<Answer> value;

	public Answers(List<Answer> value) {
		this.value = value;
	}

	public DeleteHistories deletedBy(User user) throws CannotDeleteException {
		return new DeleteHistories(this.value.stream()
			.map(answer -> answer.deletedBy(user))
			.collect(Collectors.toList()));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Answers))
			return false;
		Answers answers = (Answers)o;
		return Objects.equals(value, answers.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
