package teamficial.teamficial_be.domain.keyword.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TeamficialLogRequestDto {
    private Long userId;

    @NotBlank(message = "첫 번째 내용은 비어 있을 수 없습니다.")
    private String content1;

    @NotBlank(message = "두 번째 내용은 비어 있을 수 없습니다.")
    private String content2;

    @NotBlank(message = "세 번째 내용은 비어 있을 수 없습니다.")
    private String content3;
}
