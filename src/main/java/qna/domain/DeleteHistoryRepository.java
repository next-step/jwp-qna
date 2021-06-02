package qna.domain;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeleteHistoryRepository extends JpaRepository<DeleteHistory, Long> {

    @Query(value = "SELECT d FROM DeleteHistory d WHERE d.contentId = ?1")
    List<DeleteHistory> findByContentId(Long contentId);

    List<DeleteHistory> findByCreateDateNull();

    List<DeleteHistory> findByContentType(ContentType contentType);

    @Query(
        value = "SELECT * FROM delete_history d WHERE d.deleted_by_id in :ids",
        nativeQuery = true)
    List<DeleteHistory> findByIdsWithNative(@Param("ids") Collection<Long> ids);

    @Modifying
    @Query("update DeleteHistory d set d.contentType = :contentType where d.id = :id")
    int updateDeleteHistorySetContentTypeById(@Param("contentType") ContentType contentType, @Param("id") Long id);
}
