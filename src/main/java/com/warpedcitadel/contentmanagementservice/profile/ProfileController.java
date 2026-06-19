package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.payload.ApiResponse;
import com.warpedcitadel.contentmanagementservice.profile.dto.GameProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping(path = "/game", version = "1.0")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }


    @PostMapping("/createGameProfile")
    public ResponseEntity<ApiResponse<String>> createGameProfile(@RequestBody GameProfileDto gameProfile, WebRequest request) {

        String gameProfileUUID = profileService.createGameProfile(gameProfile);
        ApiResponse<String> response = new ApiResponse<>("Game profile Created",
                HttpStatus.OK.value(),
                gameProfileUUID,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
