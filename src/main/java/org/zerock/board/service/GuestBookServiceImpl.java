package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.GuestBookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.GuestBook;
import org.zerock.board.repository.GuestBookRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Function;

@Service // 스프링에서 빈으로 처리되도록 함
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestBookServiceImpl implements GuestBookService {

    private final GuestBookRepository repository; //반드시 final로 선언해야 함

    @Override
    public Long register(GuestBookDTO dto) {

        log.info("DTO--------------");
        log.info(dto);

        GuestBook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        Page<GuestBook> result = repository.findAll(pageable);

        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    } // entityToDTO()를 이용해서 java.util.Function 생성하고 PageResultDTO로 구성함

    @Override
    public GuestBookDTO read(Long gno) {

        Optional<GuestBook> result = repository.findById(gno);

        return result.isPresent()? entityToDTO(result.get()): null;
    }
    // PageResultDTO에는 JPQ의 처리 결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고 화면에 페이지 처리와 필요한 값들 처리
}
