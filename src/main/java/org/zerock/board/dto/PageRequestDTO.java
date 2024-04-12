package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
// 검색 처리를 담당
    private int page; // 화면에서 전달됨
    private int size; // 화면에서 전달됨

    private String type;

    private String keyword;

    public PageRequestDTO() { // jpa 쪽에서 사용하는 Pageable 타입의 객체를 생성하는데 쓰임

        this.page = 1; // 기본값
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){

        return PageRequest.of(page - 1, size, sort);
    }
}
