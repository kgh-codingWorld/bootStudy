package org.zerock.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
import org.zerock.board.entity.QGuestBook;
import org.zerock.board.repository.GuestBookRepository;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.function.Function;

@Service // 스프링에서 빈으로 처리되도록 함
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestBookServiceImpl implements GuestBookService {

    private final GuestBookRepository repository; //반드시 final로 선언해야 함

    // 방명록 등록
    @Override
    public Long register(GuestBookDTO dto) {

        log.info("DTO--------------");
        log.info(dto);

        GuestBook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);

        return entity.getGno();
    }

    // 방명록 리스트
    @Override
    public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리

        Page<GuestBook> result = repository.findAll(booleanBuilder, pageable); // Querydsl 사용

        Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    } // entityToDTO()를 이용해서 java.util.Function 생성하고 PageResultDTO로 구성함

    // 방명록 조회
    @Override
    public GuestBookDTO read(Long gno) {

        Optional<GuestBook> result = repository.findById(gno);

        return result.isPresent()? entityToDTO(result.get()): null;
    }

    // 방명록 삭제
    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    // 방명록 수정
    @Override
    public void modify(GuestBookDTO dto) {
        // 업데이트 항목 : 제목, 내용
        Optional<GuestBook> result = repository.findById(dto.getGno());

        if(result.isPresent()) {
            GuestBook entity = result.get();

            entity.changeTitle(dto.getTitle()); // 수정된 제목 넣기
            entity.changeContent(dto.getContent()); // 수정된 내용 넣기

            repository.save(entity); // 다시 저장
        }
    }
    // PageResultDTO에는 JPQ의 처리 결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고 화면에 페이지 처리와 필요한 값들 처리

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) { // Querydsl 처리

        String type = requestDTO.getType();

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QGuestBook qGuestBook = QGuestBook.guestBook;

        String keyword = requestDTO.getKeyword();

        BooleanExpression expression = qGuestBook.gno.gt(0L); // gno > 0 조건만 생성

        booleanBuilder.and(expression);

        if (type == null || type.trim().length() == 0) { // 검색 조건이 없는 경우

            return booleanBuilder;
        } // 검색 조건 type이 있으면 conditionBuilder 변수 생성해서 각 검색 조건을 or로 연결해 처리함
          // 없으면 gno > 0로만 생성

        // 검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestBook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
