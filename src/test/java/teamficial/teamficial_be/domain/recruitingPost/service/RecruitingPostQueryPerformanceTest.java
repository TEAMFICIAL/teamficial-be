package teamficial.teamficial_be.domain.recruitingPost.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import teamficial.teamficial_be.domain.recruitingPost.dto.RecruitingPostDto;
import teamficial.teamficial_be.domain.recruitingPost.entity.ProgressWay;
import teamficial.teamficial_be.domain.recruitingPost.entity.RecruitingStatus;
import teamficial.teamficial_be.domain.recruitingPost.repository.RecruitingPostRepositoryCustom;
import teamficial.teamficial_be.global.enums.Position;

@SpringBootTest
@Transactional
public class RecruitingPostQueryPerformanceTest {
    @Autowired
    RecruitingPostRepositoryCustom recruitingPostRepository;

    @Autowired
    RecruitingPostService recruitingPostService;

    @Test
    void measure_querydsl_paging_performance() {

        Pageable pageable = PageRequest.of(0, 20); // 첫 페이지, 20개 조회

        RecruitingStatus status = null;
        Position position = null;
        ProgressWay progressWay = null;

        System.out.println("==== QueryDSL Paging Performance Test ====");

        long start = System.currentTimeMillis();
        Page<RecruitingPostDto.RecruitingPostsResponseDTO> page1 =
                recruitingPostService.getRecruitingPosts(status, position, progressWay, pageable);
        long end = System.currentTimeMillis();
        System.out.println("첫페이지 조회 시간: " + (end - start) + " ms");

//        // 2) 두 번째 실행 (캐시 미스/플랜 생성된 상태)
//        long start2 = System.currentTimeMillis();
//        Page<RecruitingPostDto.RecruitingPostsResponseDTO> page2 =
//                recruitingPostService.getRecruitingPosts(status, position, progressWay, pageable);
//        long end2 = System.currentTimeMillis();
//        System.out.println("Run #2: " + (end2 - start2) + " ms");
//
//        // 3) 세 번째 실행 (DB Cache / Buffer Pool Warm)
//        long start3 = System.currentTimeMillis();
//        Page<RecruitingPostDto.RecruitingPostsResponseDTO> page3 =
//                recruitingPostService.getRecruitingPosts(status, position, progressWay, pageable);
//        long end3 = System.currentTimeMillis();
//        System.out.println("Run #3: " + (end3 - start3) + " ms");
//
//        // 페이지 내용 출력(옵션)
//        System.out.println("Total Elements: " + page3.getTotalElements());
//        System.out.println("Returned size: " + page3.getContent().size());
    }
}
