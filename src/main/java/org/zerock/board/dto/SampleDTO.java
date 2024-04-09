package org.zerock.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data // getter, setter, toString...
@Builder(toBuilder = true) // 빌더 패턴: 객체 집어넣을 때 . 찍어서 넣는 것
public class SampleDTO {
    // dto는 프론트에서 자바까지 객체를 담당함
    // entity는 db에서 자바까지 영속성을 담당함(영속 계층)
    // 나중에는 dtotoentity 또는 entitytodto라는 메서드가 이 두 개를 전이하는 역할을 담당함

    // spring과는 다르게 db까지 가지 않음
    private Long sno;
    private String first;
    private String last;
    private LocalDateTime regTime;
}
