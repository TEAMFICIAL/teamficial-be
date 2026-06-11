package teamficial.teamficial_be.domain.keyword.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KeywordContentPairDto {
    private String keyword;
    private String content;
}
