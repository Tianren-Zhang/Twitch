package com.laioffer.twitch.recommendation;


import com.laioffer.twitch.db.entity.UserEntity;
import com.laioffer.twitch.model.TypeGroupedItemList;
import com.laioffer.twitch.user.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
public class RecommendationController {


    private final RecommendationService recommendationService;
    private final UserService userService;


    public RecommendationController(
            RecommendationService recommendationService,
            UserService userService
    ) {
        this.recommendationService = recommendationService;
        this.userService = userService;
    }

    // @AuthenticationPrincipal annotation is used to get the currently authenticated user from the Spring Security context.
    // The method returns a TypeGroupedItemList, which could be a grouped list of recommended items.
    // First we check if there is an authenticated user. If there is, we retrieve the corresponding UserEntity from the UserService.
    // Then, we call the recommendItems method from the RecommendationService, passing the UserEntity
    @GetMapping("/recommendation")
    public TypeGroupedItemList getRecommendation(@AuthenticationPrincipal User user) throws IOException {
        UserEntity userEntity = null;
        if (user != null) {
            userEntity = userService.findByUsername(user.getUsername());
        }
        return recommendationService.recommendItems(userEntity);
    }
}
