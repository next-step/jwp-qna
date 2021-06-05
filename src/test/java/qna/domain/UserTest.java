package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
	public static final User JAVAJIGI = new User(1L, "javajigi", "password1", "name1", "javajigi@slipp.net");
	public static final User SANJIGI = new User(2L, "sanjigi", "password2", "name2", "sanjigi@slipp.net");

	@Autowired
	private UserRepository users;

	@DisplayName("테스트 초기화")
	@BeforeEach
	void setup() {
		모든_레포지토리_데이터_삭제();
	}

	private void 모든_레포지토리_데이터_삭제() {
		users.deleteAll();
	}

	@DisplayName("User 저장 : save()")
	@Test
	@Order(1)
	void save() {
		//given

		//when
		User actual = users.save(JAVAJIGI);
		User actual2 = users.save(SANJIGI);

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(JAVAJIGI)).isTrue(),
			() -> assertThat(actual.getPassword()).isEqualTo(JAVAJIGI.getPassword()),
			() -> assertThat(actual.getUserId()).isEqualTo(JAVAJIGI.getUserId()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.equalsNameAndEmail(SANJIGI)).isTrue(),
			() -> assertThat(actual2.getPassword()).isEqualTo(SANJIGI.getPassword()),
			() -> assertThat(actual2.getUserId()).isEqualTo(SANJIGI.getUserId())
		);
	}

	@DisplayName("User 조회 : findById()")
	@Test
	@Order(2)
	void findById() {
		//given
		User expected = users.save(JAVAJIGI);
		User expected2 = users.save(SANJIGI);

		//when
		User actual = users.findById(expected.getId()).get();
		User actual2 = users.findById(expected2.getId()).get();

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(expected)).isTrue(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.equalsNameAndEmail(expected2)).isTrue(),
			() -> assertThat(actual2.getPassword()).isEqualTo(expected2.getPassword()),
			() -> assertThat(actual2.getUserId()).isEqualTo(expected2.getUserId())
		);
	}

	@DisplayName("User 조회 : findByUserId()")
	@Test
	@Order(3)
	void findByUserId() {
		//given
		User expected = users.save(JAVAJIGI);
		User expected2 = users.save(SANJIGI);

		//when
		User actual = users.findByUserId(expected.getUserId()).get();
		User actual2 = users.findByUserId(expected2.getUserId()).get();

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(expected)).isTrue(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId()),
			() -> assertThat(actual2.getId()).isNotNull(),
			() -> assertThat(actual2.equalsNameAndEmail(expected2)).isTrue(),
			() -> assertThat(actual2.getPassword()).isEqualTo(expected2.getPassword()),
			() -> assertThat(actual2.getUserId()).isEqualTo(expected2.getUserId())
		);
	}

	@DisplayName("User 수정 : setUserId()")
	@Test
	@Order(4)
	void setUserId() {
		//given
		User expected = users.save(JAVAJIGI);
		String modifiedUserId = "pobi";

		//when
		expected.setUserId(modifiedUserId);
		User actual = users.findByUserId(modifiedUserId).get();

		//then
		assertAll(
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.equalsNameAndEmail(expected)).isTrue(),
			() -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
			() -> assertThat(actual.getUserId()).isEqualTo(expected.getUserId())
		);
	}

	@DisplayName("User 삭제 : delete()")
	@Test
	@Order(5)
	void delete() {
		//given
		User expected = users.save(JAVAJIGI);
		User beforeDeleteUser = users.findByUserId(expected.getUserId()).get();

		//when
		users.delete(expected);
		users.flush();
		Optional<User> afterDeleteUserOptional = users.findByUserId(expected.getUserId());

		//then
		assertAll(
			() -> assertThat(beforeDeleteUser).isNotNull(),
			() -> assertThat(afterDeleteUserOptional.isPresent()).isFalse()
		);
	}
}
