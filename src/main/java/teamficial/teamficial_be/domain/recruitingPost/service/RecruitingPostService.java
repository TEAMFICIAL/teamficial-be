package teamficial.teamficial_be.domain.recruitingPost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingPost;
import teamficial.teamficial_be.domain.recruitingPost.repository.RecruitingPostRepository;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.apiPayload.exception.handler.NotFoundHandler;

@Service
@RequiredArgsConstructor
public class RecruitingPostService {

    private final RecruitingPostRepository recruitingPostRepository;

    public RecruitingPost getRecruitingPostById(Long recruitingPostId) {
        return recruitingPostRepository.findById(recruitingPostId)
                .orElseThrow(()-> new NotFoundHandler(ErrorStatus.NOT_FOUND_RECRUITING_POST));
    }

    public void validatePostOwner(User user, RecruitingPost recruitingPost) {
        Long writerId = recruitingPost.getProfile().getUser().getId();
        if (!user.getId().equals(writerId)) {
            throw new GeneralException(ErrorStatus._FORBIDDEN);
        }
    }
}
