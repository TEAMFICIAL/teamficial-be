# 🗣️ 함께의 시작을 부드럽게, 팀빌딩 서비스 "팀피셜"

<img width="4560" height="3000" alt="팀피셜_웹썸네일(최종)" src="https://github.com/user-attachments/assets/70ad22ae-3f6d-4752-95f0-0c66ecf87214" />

# 프로젝트 소개
팀피셜은 스펙이 아닌 ‘함께 일해본 사람들의 진짜 이야기’로 나를 보여주는
소프트스킬 기반의 팀빌딩 플랫폼입니다.

질문 템플릿을 통해 동료들이 기록한 나의 협업 성향, 일하는 태도,
커뮤니케이션 스타일을 투명하게 제공합니다. 

팀피셜은 더 신뢰할 수 있는 협업, 더 조화로운 팀워크로 이어지는 과정
전반을 설계합니다.
<img width="1920" height="1080" alt="3" src="https://github.com/user-attachments/assets/aa97217f-b9bd-4205-a8b1-fbe9f51226f5" />
<img width="1920" height="1080" alt="4" src="https://github.com/user-attachments/assets/d771c6f1-57ce-4b1f-95a2-db80311e4af4" />
<img width="1920" height="1080" alt="5" src="https://github.com/user-attachments/assets/0ee380d1-c7ea-4a2b-8dd0-9b77f0eae134" />
<img width="1920" height="1080" alt="15" src="https://github.com/user-attachments/assets/d555f742-1730-41b4-a71a-ad25a71078ec" />
<img width="1920" height="1080" alt="19" src="https://github.com/user-attachments/assets/8f22a0c4-db4d-4df3-acc4-5b7e1e553899" />
<img width="1920" height="1080" alt="21" src="https://github.com/user-attachments/assets/bf878e94-8ccd-4388-a6e3-693847518a15" />
<img width="1920" height="1080" alt="28" src="https://github.com/user-attachments/assets/cd243c9d-4217-4173-a322-2bca81ccaeff" />


# 주요기능
<img width="1920" height="1080" alt="30" src="https://github.com/user-attachments/assets/479eaaaa-1eac-47bd-ab40-31fcbbb8d3f6" />  

🚨 프로필 수정 및 삭제 시에는, 다른 유저들에게 혼동을 주지않기 위해 제약과 에러처리를 세세하게 로직에 구현해두었습니다.  
&emsp; 코드 경로 : `src/main/java/teamficial/teamficial_be/domain/profile/dto/ProfileStatus.java`

<img width="1920" height="1080" alt="31" src="https://github.com/user-attachments/assets/cdb95aba-6dbd-4117-ae43-101d25965576" />
<img width="1920" height="1080" alt="32" src="https://github.com/user-attachments/assets/c7aa1c2c-1737-4ec9-a280-0f3c0cab80f3" />
<img width="1920" height="1080" alt="33" src="https://github.com/user-attachments/assets/fe962466-73a6-4229-9e64-e6c45c9ad0b1" />
<img width="1920" height="1080" alt="34" src="https://github.com/user-attachments/assets/53f89848-bbad-4694-8456-c1cf1996923b" />

### 프로젝트 뷰 & 지원하기

<img width="2048" height="1181" alt="Section 1" src="https://github.com/user-attachments/assets/a735dcf6-1319-4dd1-b60a-83ca70b7e618" />

🚨 메인페이지에 게시글 리스트가 있는 만큼, 게시글 리스트 조회 API 쿼리 성능 개선에 집중했습니다.
&emsp; 서브쿼리를 사용하고 관련 인덱스를 사용하여, 300만건의 데이터에서 쿼리 속도를 300ms ~ 500ms정도로 개선하였습니다.


# 기술 스택

| 역할 | 기술 / 도구 |
|------|-------------|
| **Backend** | <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/> <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white"/> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"/> <img src="https://img.shields.io/badge/JPA%2FHibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/> <img src="https://img.shields.io/badge/QueryDSL-3DDC84?style=for-the-badge&logo=google&logoColor=white"/> <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white"/> <img src="https://img.shields.io/badge/NCP-03C75A?style=for-the-badge&logo=naver&logoColor=white"/> |
<br />

# BE 팀원
<table>
  <tr>
    <th align="center" colspan="2">Backend (BE)</th>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/dldusgh318.png" width="140" alt="이연호 GitHub Avatar" /><br/>
      <a href="https://github.com/dldusgh318"><b>이연호</b></a><br/>
      <sub>Backend Lead</sub>
    </td>
    <td align="center">
      <img src="https://github.com/MINO1020.png" width="140" alt="이민호 GitHub Avatar" /><br/>
      <a href="https://github.com/MINO1020"><b>이민호</b></a><br/>
      <sub>Backend</sub>
    </td>
  </tr>
</table>

<br />

# 시스템 아키텍처
<img width="931" height="573" alt="팀피셜_아키텍처" src="https://github.com/user-attachments/assets/c846b98e-5ac2-43f2-9a7f-e617a546fcc2" />

<br />

# ERD
<img width="3070" height="1832" alt="teamficial_erd (3)" src="https://github.com/user-attachments/assets/a1396273-127e-4728-8e8e-9974a4c68aa4" />

<br />

# 데이터 파이프라인
<img width="824" height="356" alt="image" src="https://github.com/user-attachments/assets/34d7b295-02bf-4ff3-8308-5d552c621fd0" />
 
✅ 팀원이 작성해준 팀피셜록과 유사한 키워드를 찾기위해 우선 미리 선정한 키워드 100여개를 먼저 벡터 DB에 임베딩합니다.  
&emsp; 이후, 3개의 팀피셜록에 대해 각각 LLM을 이용하여 키워드 위주의 짧은 문장으로 변환합니다.  
&emsp; 그 문장을 임베딩하고, KNN 벡터 검색을 사용해 가장 의미가 유사한 키워드를 추출합니다.

# 프로젝트 구조
```
src
 └─ main
     └─ java
         └─ teamficial
             └─ teamficial_be
                 ├─ batch
                 │   ├─ job
                 │   ├─ scheduler
                 │   ├─ step
                 │   └─ tasklet
                 │
                 ├─ domain
                 │   ├─ application
                 │   ├─ auth
                 │   ├─ confirmed
                 │   ├─ keyword
                 │   ├─ myPage
                 │   ├─ profile
                 │   ├─ recruitingDetail
                 │   ├─ recruitingPost
                 │   └─ user
                 │
                 └─ global
                     ├─ apiPayload
                     ├─ config
                     ├─ entity
                     ├─ enums
                     ├─ redis
                     ├─ security
                     ├─ test
                     └─ util
```

# 관련 블로그
 - [NCP를 활용한 RAG 기반 키워드 추출](https://velog.io/@minco/NCP%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-RAG-%EA%B8%B0%EB%B0%98-%ED%82%A4%EC%9B%8C%EB%93%9C-%EC%B6%94%EC%B6%9C-%ED%8C%80%ED%94%BC%EC%85%9C)

 - [offset, limit 페이징 쿼리 최적화](https://velog.io/@minco/offset-limit-%ED%8E%98%EC%9D%B4%EC%A7%95-%EC%BF%BC%EB%A6%AC-%EC%B5%9C%EC%A0%81%ED%99%94-%EC%B2%AB-%ED%8E%98%EC%9D%B4%EC%A7%80-72s-%EB%A7%88%EC%A7%80%EB%A7%89-%ED%8E%98%EC%9D%B4%EC%A7%80-1s)
