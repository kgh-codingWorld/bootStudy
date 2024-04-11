package org.zerock.board.service;

import org.zerock.board.dto.GuestBookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.GuestBook;

public interface GuestBookService {

    Long register(GuestBookDTO dto);

    PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO);
    default GuestBook dtoToEntity(GuestBookDTO dto) { // 구현 클래스에서 동작할 수 있는 메서드
        GuestBook entity = GuestBook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity; // 서버에서 데이터베이스로
    }

    default GuestBookDTO entityToDTO(GuestBook entity){

        GuestBookDTO dto = GuestBookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto; // 데이터베이스에서 서버로
    }

    GuestBookDTO read(Long gno);
}
