package qna.study;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.collection.internal.PersistentBag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import qna.study.domain.lazy.Member;
import qna.study.domain.lazy.Team;
import qna.study.repository.MemberRepository;
import qna.study.repository.TeamRepository;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DisplayName("JPA 컬렉션 초기화 학습 테스트")
class Study1_JpaInitializeCollectionTest {

	private final EntityManager entityManager;

	public Study1_JpaInitializeCollectionTest(EntityManager entityManager, TeamRepository teamRepository,
		MemberRepository memberRepository) {
		this.entityManager = entityManager;
	}

	@Test
	@DisplayName("List 는 영속성 컨텍스트에 저장 시 PersistentBag 으로 wrapping 된다.")
	void name() {
		// given
		Team team = new Team("team1");
		List<Member> members = team.getMembers();
		assertThat(members.getClass()).isEqualTo(ArrayList.class);

		// when
		entityManager.persist(team);
		List<Member> membersAfterFlush = team.getMembers();

		/**
		 * JPA 컬렉션을 필드에서 바로 초기화 하는 것이 안전한 이유
		 * 1. NPE 발생 방지
		 * 2. List 는 영속성 컨텍스트에 저장 시 PersistentBag 으로 wrapping 된다.
		 *    컬렉션을 잘못 생성하면 하이버네이트 내부 메커니즘 문제 발생 가능성 때문에 필드에서 빠르게 초기화 하는 것이 안전하다.
		 */
		// then
		assertThat(membersAfterFlush.getClass()).isEqualTo(PersistentBag.class);
	}
}
