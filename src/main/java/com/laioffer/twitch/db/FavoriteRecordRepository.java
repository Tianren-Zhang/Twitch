package com.laioffer.twitch.db;

import com.laioffer.twitch.db.entity.FavoriteRecordEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
// Spring Data will provide a proxy implementation at runtime.
public interface FavoriteRecordRepository extends ListCrudRepository<FavoriteRecordEntity, Long> {
    // Returns a list of favorite records for a specific user.
    List<FavoriteRecordEntity> findAllByUserId(Long userId);

    // Checks if a favorite record exists for a specific user and item.
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    // Returns a list of favorite item IDs for a specific user.
    @Query("SELECT item_id FROM favorite_records WHERE user_id = :userId")
    List<Long> findFavoriteItemIdsByUserId(Long userId);

    // Deletes a favorite record for a specific user and item.
    // @Modifying indicates that this query will modify data
    @Modifying
    @Query("DELETE FROM favorite_records WHERE user_id = :userId AND item_id = :itemId")
    void delete(Long userId, Long itemId);
}