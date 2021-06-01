package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;
import static qna.domain.UserTest.SANJIGI;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DeleteHistoryRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private DeleteHistoryRepository deleteHistorys;

	@Autowired
	private UserRepository users;

	private User deleteByUser;
	private DeleteHistory expected;

	@BeforeEach
	void setUp(){
		// given
		deleteByUser = users.save(JAVAJIGI);
		expected = deleteHistorys.save(new DeleteHistory(ContentType.ANSWER, 0l, deleteByUser, LocalDateTime.now()));

		assertAll(() -> {
			assertThat(deleteByUser).isNotNull();
			assertThat(expected).isNotNull();
		});
	}


	@Test
	@DisplayName("save 테스트")
	void saveTest() {
		// when
		assertAll(() -> {
			assertThat(expected.isSameContent(ContentType.ANSWER)).isTrue();
			assertThat(expected.isSameOwner(deleteByUser)).isTrue();
			assertThat(expected.isSameContentId(0l)).isTrue();
		});
	}

	@Test
	@DisplayName("update 테스트")
	void updateTest() {
		// when
		expected.setContentType(ContentType.QUESTION);

		// then
		assertThat(deleteHistorys.findById(expected.getId()))
			.isPresent()
			.get()
			.extracting(value -> value.isSameContent(ContentType.QUESTION))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("연관관계 deletedByUser 업데이트 테스트")
	void updateDeleteUserTest() {
		// given
		User updateDeleteByUser = users.save(SANJIGI);

		// when
		expected.toDeletedByUser(updateDeleteByUser);

		// then
		// then
		assertThat(deleteHistorys.findById(expected.getId()))
			.isPresent()
			.get()
			.extracting(deleteHistory -> deleteHistory.isSameOwner(updateDeleteByUser))
			.isEqualTo(true);
	}

	@Test
	@DisplayName("findById 테스트")
	void findByIdTest() {
		// when
		assertThat(deleteHistorys.findById(expected.getId()))
			.isPresent()
			.get()
			.isSameAs(expected);
	}

	@Test
	@DisplayName("연관관계 매핑이 된 deletedByUser를 지연 로딩을 통해 불러오는지 테스트")
	void fetchLazyTest() {
		testEntityManager.clear(); // cache clear

		assertThat(deleteHistorys.findById(expected.getId())) // select deleteHistory
			.isPresent()
			.get()
			.extracting(deleteHistory -> deleteHistory.getDeletedByUser()) // select user
			.isEqualTo(deleteByUser);
	}

	@Test
	@DisplayName("삭제 테스트")
	void deleteTest() {
		// when
		deleteHistorys.delete(expected);

		// then
		assertThat(deleteHistorys.findById(expected.getId()))
			.isNotPresent();
	}

}
