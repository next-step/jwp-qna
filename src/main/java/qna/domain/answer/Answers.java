package qna.domain.answer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import qna.CannotDeleteException;
import qna.domain.deletehistory.DeleteHistories;
import qna.domain.user.User;

@Embeddable
public class Answers {

	@OneToMany(mappedBy = "question",
		fetch = FetchType.LAZY,
		cascade = CascadeType.ALL,
		orphanRemoval = true
	)
	@Column(name = "answers")
	private List<Answer> value = new ArrayList<>();

	public Answers(List<Answer> value) {
		this.value = value;
	}

	protected Answers() { }

	public DeleteHistories deletedBy(User user) throws CannotDeleteException {

		return new DeleteHistories(getValue().stream()
			.map(answer -> answer.deletedBy(user))
			.collect(Collectors.toList()));
	}

	public void add(Answer answer) {
		this.value.add(answer);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Answers))
			return false;
		Answers answers = (Answers)o;
		return Objects.equals(answers, answers.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	public List<Answer> getValue() {
		return this.value;
	}
}
