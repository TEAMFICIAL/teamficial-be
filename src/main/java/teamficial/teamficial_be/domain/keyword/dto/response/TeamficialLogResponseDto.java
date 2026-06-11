package teamficial.teamficial_be.domain.keyword.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamficialLogResponseDto {
    List<KeywordContentPairDto> contentPairs;
}

