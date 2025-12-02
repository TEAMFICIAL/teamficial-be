package teamficial.teamficial_be.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class ScrollResponse<T> {
    private List<T> data;
    private boolean hasNext;
    private int nextPage;

    public static <T> ScrollResponse<T> of(Slice<T> slice) {
        return new ScrollResponse<>(
                slice.getContent(),
                slice.hasNext(),
                slice.getNumber() + 1
        );
    }
}
