package study.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
