package com.laioffer.twitch.external;

import com.laioffer.twitch.external.model.Clip;
import com.laioffer.twitch.external.model.Game;
import com.laioffer.twitch.external.model.Stream;
import com.laioffer.twitch.external.model.Video;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TwitchService {

    private final TwitchApiClient twitchApiClient;

    public TwitchService(TwitchApiClient twitchApiClient) {
        this.twitchApiClient = twitchApiClient;
    }
    // Cacheable annotation means that their results will be cached under the cache names "top_games" and "games_by_name" respectively.
    // Subsequent calls with the same parameters will retrieve the results from the cache instead of making a fresh API call, until the cache is invalidated or expires.
    @Cacheable("top_games")
    public List<Game> getTopGames() {
        return twitchApiClient.getTopGames().data();
    }

    @Cacheable("games_by_name")
    public List<Game> getGames(String name) {
        return twitchApiClient.getGames(name).data();
    }

    public List<Stream> getStreams(List<String> gameIds, int first) {
        return twitchApiClient.getStreams(gameIds, first).data();
    }

    public List<Video> getVideos(String gameId, int first) {
        return twitchApiClient.getVideos(gameId, first).data();
    }

    public List<Clip> getClips(String gameId, int first) {
        return twitchApiClient.getClips(gameId, first).data();
    }

    public List<String> getTopGameIds() {
        List<String> topGameIds = new ArrayList<>();
        for (Game game : getTopGames()) {
            topGameIds.add(game.id());
        }
        return topGameIds;
    }
}