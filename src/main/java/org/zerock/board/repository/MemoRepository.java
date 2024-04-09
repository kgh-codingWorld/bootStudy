package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Memo;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    // JPA 레포지토리는 인터페이스 자체이고 JpaRepository 인터페이스를 상속하는 것 만드로 모든 작없이 끝남.
    // extends JpaRepository<엔티티, pk타입>

    // insert 작업 : save(엔티티 객체)
    // select 작업 : findById(키 타입), getOne(키 타입)
    // update 작업 : save(엔티티 객체)
    // delete 작업 : deleteById(키 타입),  delete(엔티티 객체)

    // 쿼리 메서드 (메서드 명이 쿼리를 대체함)
    // https://docs.spring.io/spring-data/jpa/docs/current-SNAPSHOT/reference/html/#jpa.query-methods
    // https://docs.spring.io/spring-data/jpa/docs/current-SNAPSHOT/reference/html/#jpa.query-methods.query-creation

    // 1
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);
    // mno를 기준으로 between 구문을 사용하고 Orderby가 적용
    // 매개값으로 받은 Long from, Long to 는 Between and 구문
    // 리턴 타입은 List<Memo> -> 리스트 타입에 객체는 Memo

    // 2
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);
    // 페이징 처리 추가용 메서드
    // Page<Memo> 리턴 타입 -> 페이징 타입에 객체는 Memo
    // 매개값으로 받은 from ~ to 까지 select 진행하여 페이징 타입으로 리턴하는 쿼리 메서드

    // 3
    // 예를 들어 10보다 작은 데이터를 삭제한다.
    void deleteMemoByMnoLessThan(Long num);
    // 삭제용 쿼리 메서드
    // LessThan 이하

    // 4
    // @Query 는 쿼리메서드의 단순함을 넘어서는 수동용 쿼리
    // 단 엔티티를 이용하기 때문에 대소문자 조심(JPQL)
    // 세미콜론 사용 안 함
    @Query("SELECT m FROM Memo m ORDER BY m.mno DESC ")
    List<Memo> getListDesc(); // 내가 만든 메서드명

    //5
    // 매개값이 있는 @Query문 :값(타입으로 받음)
    @Transactional
    @Modifying
    @Query("UPDATE Memo m SET m.memoText = :memoText WHERE m.mno = :mno ")
    int updateMemoText(@Param("mno") long mno, @Param("memoText") String memoText);
    // ?1, ?2 -> 1부터 시작하는 파라미터의 순서를 이용(pstmt)
    // :XXX   ->  :파라미터 이름을 활용
    // :#{  } -> 자바 빈 스타일

    // 6
    // 매개값이 객체(빈)으로 들어올 경우
    @Transactional
    @Modifying
    @Query("UPDATE Memo m SET m.memoText = :#{#memoBean.memoText} WHERE m.mno = :#{#memoBean.mno} ")
    int updateMemoTextBean(@Param("memoBean") Memo memo);
    // :#{  } -> 자바 빈 스타일

    // 7
    // @Query 메서드로 페이징 처리 해보기 -> 리턴 타입이 Page<Memo>
    // 쿼리문 두 개 사용 시 value 사용, 페이지 처리 시 countQuery 사용
    @Query(value = "SELECT m FROM Memo m WHERE m.mno > :mno", countQuery = "SELECT count(m) FROM Memo m WHERE m.mno > : mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);
    // @Query를 이용하는 경우 Pageable 타입의 파라미터를 적용하면 페이징 처리와 정렬에 대한 부분을 생략할 수 있다.
    // 리턴타입을 Page<엔티티>로 지정하면 count를 처리하는 쿼리를 적용한다.

    // 8
    // DB에 존재하지 않는 값 처리: 날짜 등
    // @Query 메서드로 페이징 처리 해보기 -> 리턴 타입이 Page<Object[]>: Object가 최상위 클래스이기 때문에 모든 타입을 사용 가능
    @Query(value = "SELECT m.mno, m.memoText, CURRENT_DATE FROM Memo m WHERE m.mno > :mno", countQuery = "select count(m) from Memo m where m.mno > : mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);
    // 페이지타입의 제네릭을 Object로 선언하면 적당한 엔티티 타입이 존재하지 않는 경우 리턴 타입으로 지정함
    // CURRENT_DATE는 db에 있는 시간 정보를 가져옴

    //9
    // Native Sql 처리 : DB용 쿼리로 사용하는 기법
    // 엔티티 대신 테이블 명을 사용해야 함
    @Query(value = "SELECT * FROM memo WHERE mno >0 ", nativeQuery = true)
    List<Object[]> getNativeResult();
    // 복잡한 sql 문을 모두 사용할 수 있음 nativeQuery = true (일반 sql문 사용) 단 엔티티 활용
}