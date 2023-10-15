package com.laioffer.twitch.external;

import com.laioffer.twitch.external.model.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
// Expose endpoints related to the game entity in the context of the Twitch service:
@RestController
public class GameController {

    private final TwitchService twitchService;

    public GameController(TwitchService twitchService) {
        this.twitchService = twitchService;
    }
    // Get the top games or search for games by name
    @GetMapping("/game")
    public List<Game> getGames(@RequestParam(value = "game_name", required = false) String gameName) {
        if (gameName == null) {
            return twitchService.getTopGames();
        } else {
            return twitchService.getGames(gameName);
        }
    }
}