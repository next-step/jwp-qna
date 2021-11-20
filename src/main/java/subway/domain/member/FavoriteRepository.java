package subway.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.member.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

}
