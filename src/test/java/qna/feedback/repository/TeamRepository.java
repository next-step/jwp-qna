package qna.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qna.feedback.entity.lazy.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team as t join t.members as m where t.id = :id")
    Team getTeamByIdByJpqlJoin(@Param("id") Long teamId);

    @Query("select t from Team as t join fetch t.members as m where t.id = :id")
    Team getTeamByIdByJpqlFetchJoin(@Param("id") Long teamId);
}
