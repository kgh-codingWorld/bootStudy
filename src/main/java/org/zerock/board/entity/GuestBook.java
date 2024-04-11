package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 테이블 자동 관리
@Getter
@Builder
@AllArgsConstructor // 빌더패턴에 필수적임
@NoArgsConstructor // 빌더패턴에 필수적임
@ToString // 객체가 아닌 문자열로 변환시켜줌
public class GuestBook extends BaseEntity { // BaseEntity를 상속 받아 날짜를 동기화 시킴

    @Id // guestbook 테이블에 pk 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mariadb 용 자동 번호
    private Long gno; // 방명록에서 사용할 번호

    @Column(length = 100, nullable = false)
    private String title; // 제목

    @Column(length = 1500, nullable = false)
    private String content; // 내용

    @Column(length = 50, nullable = false)
    private String writer; // 작성자

    public void changeTitle(String title) {
        this.title = title; // 세터 역할(수정 시 사용)
    }

    public void changeContent(String content) {
        this.content = content; // 세터 역할(수정 시 사용)
    }
}
