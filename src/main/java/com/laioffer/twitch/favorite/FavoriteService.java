package com.laioffer.twitch.favorite;

import com.laioffer.twitch.db.FavoriteRecordRepository;
import com.laioffer.twitch.db.ItemRepository;
import com.laioffer.twitch.db.entity.FavoriteRecordEntity;
import com.laioffer.twitch.db.entity.ItemEntity;
import com.laioffer.twitch.db.entity.UserEntity;
import com.laioffer.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class FavoriteService {
    private final ItemRepository itemRepository;
    private final FavoriteRecordRepository favoriteRecordRepository;

    public FavoriteService(ItemRepository itemRepository, FavoriteRecordRepository favoriteRecordRepository) {
        this.itemRepository = itemRepository;
        this.favoriteRecordRepository = favoriteRecordRepository;
    }
    // @CacheEvict: when this method is called, any cache entry with a name of recommend_items and a key that matches the first user will be removed.
    // @Transactional: operations inside this method will either all succeed or all fail.
    // marking an item as a favorite for a user
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    @Transactional
    public void setFavoriteItem(UserEntity user, ItemEntity item) throws DuplicateFavoriteException {
        ItemEntity persistedItem = itemRepository.findByTwitchId(item.twitchId());
        if (persistedItem == null) {
            persistedItem = itemRepository.save(item);
        }
        if (favoriteRecordRepository.existsByUserIdAndItemId(user.id(), persistedItem.id())) {
            throw new DuplicateFavoriteException();
        }
        FavoriteRecordEntity favoriteRecord = new FavoriteRecordEntity(null, user.id(), persistedItem.id(), Instant.now());
        favoriteRecordRepository.save(favoriteRecord);
    }
    // removes an item from a user's list of favorites.
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    public void unsetFavoriteItem(UserEntity user, String twitchId) {
        ItemEntity item = itemRepository.findByTwitchId(twitchId);
        if (item != null) {
            favoriteRecordRepository.delete(user.id(), item.id());
        }
    }

    public List<ItemEntity> getFavoriteItems(UserEntity user) {
        List<Long> favoriteItemIds = favoriteRecordRepository.findFavoriteItemIdsByUserId(user.id());
        return itemRepository.findAllById(favoriteItemIds);
    }

    public TypeGroupedItemList getGroupedFavoriteItems(UserEntity user) {
        List<ItemEntity> favoriteItems = getFavoriteItems(user);
        return new TypeGroupedItemList(favoriteItems);
    }
}