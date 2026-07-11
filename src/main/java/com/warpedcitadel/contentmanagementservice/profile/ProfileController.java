package com.warpedcitadel.contentmanagementservice.profile;

import com.warpedcitadel.contentmanagementservice.payload.ApiResponse;
import com.warpedcitadel.contentmanagementservice.profile.dto.CloudFrontCookie;
import com.warpedcitadel.contentmanagementservice.profile.dto.DeleteGameProfileDto;
import com.warpedcitadel.contentmanagementservice.profile.dto.GameProfileDetailsDto;
import com.warpedcitadel.contentmanagementservice.profile.dto.GameProfileDto;
import com.warpedcitadel.contentmanagementservice.profile.util.CloudFrontCookieMaker;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.Instant;

@RestController
@RequestMapping(path = "/api/game", version = "1.0")
public class ProfileController {

    private final ProfileService profileService;
    private final CloudFrontCookieMaker cloudFrontCookieMaker;

    public ProfileController(ProfileService profileService,
                             CloudFrontCookieMaker cloudFrontCookieMaker) {
        this.profileService = profileService;
        this.cloudFrontCookieMaker = cloudFrontCookieMaker;
    }


    @PostMapping("/createGameProfile")
    public ResponseEntity<ApiResponse<String>> createGameProfile(@RequestBody GameProfileDto gameProfile, WebRequest request) {

        String gameProfileUUID = profileService.createGameProfile(gameProfile);
        ApiResponse<String> response = new ApiResponse<>("CREATED",
                HttpStatus.OK.value(),
                gameProfileUUID,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/updateGameProfile")
    public ResponseEntity<ApiResponse<String>> updateGameProfile(@RequestBody GameProfileDto gameProfile, WebRequest request) {

        profileService.updateGameProfile(gameProfile);
        ApiResponse<String> response = new ApiResponse<>("UPDATE",
                HttpStatus.OK.value(),
                "Game profile updated",
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getGameProfile/{uuid}")
    public ResponseEntity<ApiResponse<GameProfileDetailsDto>> getGameProfile(@PathVariable String uuid, WebRequest request,
                                                                             HttpServletResponse response) {

        GameProfileDetailsDto gameProfile = profileService.getGameProfile(uuid);
        CloudFrontCookie cookie = cloudFrontCookieMaker.generateSignedCookie(gameProfile);

        addCookie(response,
                "CloudFront-Policy",
                cookie.policy());

        addCookie(response,
                "CloudFront-Signature",
                cookie.signature());

        addCookie(response,
                "CloudFront-Key-Pair-Id",
                cookie.keyPairId());

        ApiResponse<GameProfileDetailsDto> gameProfileDetails = new ApiResponse<>("Game profile details",
                HttpStatus.OK.value(),
                gameProfile,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(gameProfileDetails, HttpStatus.OK);
    }


    @DeleteMapping("/deleteGameProfile")
    public ResponseEntity<ApiResponse<DeleteGameProfileDto>> deleteGameProfile(@RequestBody DeleteGameProfileDto gameProfile, WebRequest request) {

        profileService.deleteGameProfile(gameProfile);
        ApiResponse<DeleteGameProfileDto> response = new ApiResponse<>("Game profile deleted",
                HttpStatus.OK.value(),
                gameProfile,
                request.getDescription(false).replace("uri=", ""),
                Instant.now(Clock.systemUTC()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // ### HELPER Function ###

    // TODO: Move to content-management-service
    private void addCookie(
            HttpServletResponse response,
            String name,
            String value) {

        ResponseCookie cookie =
                ResponseCookie
                        .from(name, value)
                        .secure(true)
                        .httpOnly(true)
                        .sameSite("None")
                        .path("/")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString());
    }
}
