package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO { // Movie 클래스(엔티티)를 기준으로 작성

    private Long mno; // 영화 번호
    private String title; // 영화 제목

    @Builder.Default // 빌더로 인스턴스 생성 시 초기화할 값을 정할 수 있음
    private List<MovieImageDTO> imageDTOList = new ArrayList<>(); // 배열 객체 생성해서 넣은 변수 imageDTOList가 MovieImageDTO 타입의 객체로 List 배열에 들어감
}
