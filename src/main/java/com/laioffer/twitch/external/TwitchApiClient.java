package com.laioffer.twitch.external;

import com.laioffer.twitch.external.model.ClipResponse;
import com.laioffer.twitch.external.model.GameResponse;
import com.laioffer.twitch.external.model.StreamResponse;
import com.laioffer.twitch.external.model.VideoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

// Spring Cloud generate a proxy at runtime that implements this interface.
// The responses from the HTTP calls are automatically deserialized into the return types specified in the interface.
@FeignClient(name = "twitch-api")
public interface TwitchApiClient {


    @GetMapping("/games")
    GameResponse getGames(@RequestParam("name") String name);


    @GetMapping("/games/top")
    GameResponse getTopGames();


    @GetMapping("/videos/")
    VideoResponse getVideos(@RequestParam("game_id") String gameId, @RequestParam("first") int first);


    @GetMapping("/clips/")
    ClipResponse getClips(@RequestParam("game_id") String gameId, @RequestParam("first") int first);


    @GetMapping("/streams/")
    StreamResponse getStreams(@RequestParam("game_id") List<String> gameIds, @RequestParam("first") int first);


}

