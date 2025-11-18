package teamficial.teamficial_be.domain.recruitingPost.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    @PersistenceContext
    private EntityManager em;

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

    }

    @Test
    void measure_querydsl_last_page_performance() {

        RecruitingStatus status = null;
        Position position = null;
        ProgressWay progressWay = null;

        int pageSize = 20;

        // 1) 전체 개수 조회
        Long totalCount = em.createQuery(
                "SELECT COUNT(rp) FROM RecruitingPost rp", Long.class
        ).getSingleResult();

        // 2) 마지막 페이지 번호 계산
        int lastPageNumber = (int) (totalCount == 0 ? 0 : (totalCount - 1) / pageSize);

        Pageable pageable = PageRequest.of(lastPageNumber, pageSize);

        System.out.println("==== QueryDSL Last Page Performance Test ====");
        System.out.println("totalCount       = " + totalCount);
        System.out.println("pageSize         = " + pageSize);
        System.out.println("lastPageNumber   = " + lastPageNumber);
        System.out.println("---------------------------------------------");

        long start = System.currentTimeMillis();
        Page<RecruitingPostDto.RecruitingPostsResponseDTO> lastPage =
                recruitingPostService.getRecruitingPosts(status, position, progressWay, pageable);
        long end = System.currentTimeMillis();

        System.out.println("마지막 페이지 조회 시간 : " + (end - start) + " ms");
        System.out.println("조회된 데이터 수       : " + lastPage.getContent().size());
    }

}
