package teamficial.teamficial_be.domain.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import teamficial.teamficial_be.domain.profile.entity.WorkingTime;
import teamficial.teamficial_be.global.enums.Position;

@Getter
public class ProfileRequestDto {
    @Schema(description = "프로필 이름", example="1")
    @NotBlank(message = "프로필 이름은 필수입니다.")
    private String profileName;
    @Schema(description = "프로필 파트", example="FRONTEND")
    @NotNull(message = "파트는 필수입니다.")
    private Position position;
    @Schema(description = "근무 시간대", example="MORNING")
    @NotNull(message = "근무 시간대는 필수입니다.")
    private WorkingTime workingTime;
    @Schema(description = "관련 링크")
    private String link;
    @Schema(description = "연락 수단", example="오픈채팅방 링크")
    private String contactWay;
}
