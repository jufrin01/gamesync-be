package project.gamesync.dto.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String username;
    private String avatarUrl;
    private String bio;
}