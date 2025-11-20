package teamficial.teamficial_be.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.Request;
import org.opensearch.client.Response;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentFactory;
import org.opensearch.common.xcontent.json.JsonXContent;
import org.opensearch.core.xcontent.DeprecationHandler;
import org.opensearch.core.xcontent.NamedXContentRegistry;
import org.opensearch.core.xcontent.XContentBuilder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.keyword.dto.request.HeadKeywordRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.request.TeamficialLogRequestDto;
import teamficial.teamficial_be.domain.keyword.dto.response.*;
import teamficial.teamficial_be.domain.keyword.entity.HeadKeyword;
import teamficial.teamficial_be.domain.keyword.entity.Keyword;
import teamficial.teamficial_be.domain.keyword.entity.KeywordComment;
import teamficial.teamficial_be.domain.profile.entity.Profile;
import teamficial.teamficial_be.domain.profile.service.ProfileService;
import teamficial.teamficial_be.domain.user.entity.User;
import teamficial.teamficial_be.domain.user.service.UserService;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;
import teamficial.teamficial_be.global.util.PagedResponse;
import teamficial.teamficial_be.global.util.ScrollResponse;

import org.springframework.ai.chat.model.ChatModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamficialLogService {

    private final ChatModel chatModel;
    private final RestHighLevelClient client;

    private final ProfileService profileService;
    private final KeywordService keywordService;
    private final HeadKeywordService headKeywordService;
    private final KeywordCommentService keywordCommentService;
    private final UserService userService;
    private final EmbeddingService embeddingService;
    private final PromptLoadService promptLoadService;

    @Transactional(readOnly = true)
    public HeadKeywordResponseDto getHeadKeyword(Long profileId) {
        Profile profile = profileService.getProfileById(profileId);

        List<HeadKeyword> headKeywords= headKeywordService.getAllByProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, headKeywords);
    }


    @Transactional
    public HeadKeywordResponseDto updateHeadKeyword(User user, Long profileId, HeadKeywordRequestDto requestDto) {

        Profile profile = profileService.getProfileById(profileId);

        if (!profile.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus.PROFILE_FORBIDDEN);
        }

        if (profile.getHeadKeywords() !=null) {
            profile.getHeadKeywords().clear();
        }


        //대표키워드 설정
        if (requestDto.getKeywordIds() != null) {
            for (Long keywordId : requestDto.getKeywordIds()) {

                Keyword keyword = keywordService.getKeywordById(keywordId);

                if (!keyword.getUser().getId().equals(user.getId())) {
                    throw new GeneralException(ErrorStatus.KEYWORD_FORBIDDEN);
                }

                keyword.updateHead();
                keywordService.saveKeyword(keyword);

                HeadKeyword headKeyword = HeadKeyword.builder()
                        .profile(profile)
                        .keywordName(keyword.getKeywordName())
                        .build();

                profile.getHeadKeywords().add(headKeyword);
                log.info("headKeyword: {}", headKeyword.getKeywordName());

            }
        }

        profileService.saveProfile(profile);

        return HeadKeywordResponseDto.fromKeyword(profile, profile.getHeadKeywords());
    }

    @Transactional(readOnly = true)
    public PagedResponse<KeywordResponseDto> getKeywordList(User viewer,Long userId, int page, int size) {
        User user = userService.getUserById(userId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Keyword> keywordPage = keywordService.getAllKeywordByUser(user,pageable);

        Page<KeywordResponseDto> dtoPage = keywordPage.map(KeywordResponseDto::from);

        return PagedResponse.of(dtoPage);
    }

    @Transactional(readOnly = true)
    public ScrollResponse<KeywordCommentResponseDto> getKeywordCommentList(User user,Long keywordId, int page, int size) {
        Keyword keyword = keywordService.getKeywordById(keywordId);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<KeywordComment> keywordComments = keywordCommentService.getAllByKeyword(keyword,pageable);

        Slice<KeywordCommentResponseDto> dtoList = keywordComments.map(KeywordCommentResponseDto::from);

        return ScrollResponse.of(dtoList);
    }

    public TeamficialLogResponseDto createTeamficialLog(TeamficialLogRequestDto req) throws IOException {

        List<String> contents = List.of(
                req.getContent1(),
                req.getContent2(),
                req.getContent3()
        );

        List<KeywordContentPairDto> results = new ArrayList<>();

        for (String content : contents) {
            String prompt = promptLoadService.loadVectorSummaryPrompt(content);

            String vectorText = chatModel.call(prompt).trim();

            float[] vector = embeddingService.embed(vectorText);

            String bestKeyword = getBestKeyword(vector);

            keywordService.saveBestKeyword(req, content, bestKeyword, results);
        }

        return new TeamficialLogResponseDto(results);
    }

    private String getBestKeyword(float[] vector) throws IOException {
        String queryJson = buildKeywordKnnQuery(vector);

        Request request = new Request("POST", "/keyword_embeddings/_search");
        request.setJsonEntity(queryJson);

        Response response = client.getLowLevelClient().performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());

        SearchResponse searchResponse = SearchResponse.fromXContent(
                JsonXContent.jsonXContent.createParser(
                        NamedXContentRegistry.EMPTY,
                        DeprecationHandler.THROW_UNSUPPORTED_OPERATION,
                        responseBody
                )
        );

        String bestKeyword = (String) searchResponse.getHits().getHits()[0]
                .getSourceAsMap().get("keyword");
        return bestKeyword;
    }

    private String buildKeywordKnnQuery(float[] embeddingVector) throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("size", 1);

            builder.startObject("query");
            {
                builder.startObject("knn");
                {
                    builder.startObject("embedding");
                    {
                        builder.field("vector", embeddingVector);
                        builder.field("k", 1);
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
        }
        builder.endObject();

        return builder.toString();
    }


    public TeamficialLogRequesterResponseDto getTeamficialLogRequester(String requesterUuid) {
        User user = userService.getUserByUuid(requesterUuid);

        return TeamficialLogRequesterResponseDto.builder()
                .userId(user.getId())
                .requesterName(user.getName())
                .build();
    }
}
