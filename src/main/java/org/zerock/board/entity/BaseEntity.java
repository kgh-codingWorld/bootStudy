package org.zerock.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // 직접적인 테이블용은 아님을 명시
@Getter
@EntityListeners(value = {AuditingEntityListener.class}) // 세터 대신 사용하는 감시용 코드(데이터 변경 감지 -> 적용: 메인 메서드에 추가 코드 넣어줘야 함)
abstract class BaseEntity { // 테이블의 공통 부분을 상속시켜주는 클래스

    @CreatedDate // 게시물 생성 시 동작
    @Column(name = "regdate", updatable = false) // db 테이블의 필드명 지정, 업데이트 안 되게 강제 적용시킴
    private LocalDateTime regDate; // 게시물 등록 시간

    @LastModifiedDate // 게시물 수정 시 동작
    @Column(name = "moddate") // db 테이블의 필드명 지정
    private LocalDateTime modDate; // 게시물 수정 시간
} // 현재 사용하는 Guestbook의 엔티티 클래스 이외에도 다른 엔티티 클래스에서도 사용 가능 -> 코드 중복 방지
