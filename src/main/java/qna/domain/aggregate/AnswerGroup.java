package qna.domain.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import qna.domain.entity.Answer;
import qna.domain.entity.DeleteHistory;
import qna.domain.entity.User;

@Embeddable
public class AnswerGroup {

	@OneToMany(mappedBy = "question", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	private List<Answer> answers = new ArrayList<>();

	protected AnswerGroup() {
	}

	public static AnswerGroup generate() {
		return new AnswerGroup();
	}

	public int size() {
		return answers.size();
	}

	public void add(Answer answer) {
		answers.add(answer);
	}

	public boolean containsAll(AnswerGroup answerGroup) {
		return answers.containsAll(answerGroup.answers);
	}

	public boolean isEmpty() {
		return answers.isEmpty();
	}

	public DeleteHistoryGroup deleteAll(User loginUser) {
		DeleteHistoryGroup deleteHistoryGroup = DeleteHistoryGroup.generate();
		answers.stream()
			.map(answer -> answer.delete(loginUser))
			.forEach(deleteHistoryGroup::add);
		return deleteHistoryGroup;
	}

	public List<Answer> answers() {
		return answers;
	}
}
