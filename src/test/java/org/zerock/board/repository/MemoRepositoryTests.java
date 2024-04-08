package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.board.entity.Memo;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //부트용 테스트 코드임을 명시
public class MemoRepositoryTests {

    @Autowired //생성자 자동 주입
    MemoRepository memoRepository;

    @Test
    public void testClass(){
        //객체 주입 테스트(MemoRepository는 인터페이스임을 기억할 것)
        //인터페이스는 구현체가 있어야 함
        System.out.println("memoRepository.getClass().getName():" + memoRepository.getClass().getName());
        //memoRepository에 생성된 객체의 클래스명과 이름 알아보기
        //콘솔 결과: jdk.proxy3.$Proxy120(동적 프록시: 인터페이스의 실행 구현 클래스)
    }

    @Test
    public void testInsertDummies(){
        //memo 테이블에 더미데이터 추가
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Memo memo = Memo.builder()
                    .memoText("sample......" + i)
                    .build(); //Memo 클래스에 memoText(1~100) 생성 반복
            memoRepository.save(memo); // .save(jpa 상속으로 사용)
            //save 없으면 insert, 있으면 update
        });
    }

    @Test
    public void testSelect(){
        //있는 정보 가져오기(mno 이용)
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno);
        //.findById(mno) -> select * from 표 where mno = 100;
        //.findById(): 쿼리가 미리 실행되고나서 결과 출력됨

        System.out.println("======================================");
        if(result.isPresent()){
            Memo memo = result.get();
            System.out.println(memo); //엔티티에 toString 되어 있음
        }
    }

    @Transactional
    @Test
    public void testSelect2(){
        Long mno = 100L;
        Memo memo = memoRepository.getOne(mno); //보안 때문에 현재 차단된 메서드, SQL문 호출전에 결과부터 출력되기 때문에 좋지 않음

        System.out.println("=====================================");
        System.out.println(memo);
    }

    @Test
    public void updateTest(){
        Memo memo = Memo.builder()
                .mno(300L)
                .memoText("수정된 텍스트 테스트")
                .build();
        System.out.println(memoRepository.save(memo));
//        Hibernate:
//        select
//        m1_0.mno,
//                m1_0.memo_text
//        from
//        tbl_memo m1_0
//        where
//        m1_0.mno=?
//        Hibernate:
//        update
//                tbl_memo
//        set
//        memo_text=?
//        where
//        mno=?
//        Memo(mno=100, memoText=수정된 텍스트 테스트)

    }

    @Test
    public void testDelete(){
        Long mno = 300L;
        memoRepository.deleteById(mno);
    }

    @Test
    public void testPageDefault() {
        // jpa에 내장된 페이징, 정렬 기법 활용

        Sort sort1 = Sort.by("mno").descending();
        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); //내림차순 번호 and 메모 텍스트 오름차순
        Pageable pageable = PageRequest.of(0, 10, sortAll);

        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);


        //Hibernate:
        //    select
        //        m1_0.mno,
        //        m1_0.memo_text
        //    from
        //        tbl_memo m1_0
        //    limit
        //        ?, ?
        //Hibernate:
        //    select
        //        count(m1_0.mno)
        //    from
        //        tbl_memo m1_0
        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances

        System.out.println("---------------------------------------");

        System.out.println("Total Pages: "+result.getTotalPages()); // 총 몇 페이지

        System.out.println("Total Count: "+result.getTotalElements()); // 전체 개수

        System.out.println("Page Number: "+result.getNumber()); // 현재 페이지 번호

        System.out.println("Page Size: "+result.getSize()); // 페이지당 데이터 개수

        System.out.println("has next page?: "+result.hasNext());    // 다음 페이지 존재 여부

        System.out.println("first page?: "+result.isFirst());  //  시작페이지 여부

        //Page 1 of 10 containing org.zerock.boardboot.entity.Memo instances
        //---------------------------------------
        //Total Pages: 10
        //Total Count: 99
        //Page Number: 0
        //Page Size: 10
        //has next page?: true
        //first page?: true

        System.out.println("-----------------------------------");

        for(Memo memo : result.getContent()) {
            System.out.println(memo);
            //-----------------------------------
            //Memo(mno=1, memoText=Sample....1)
            //Memo(mno=2, memoText=Sample....2)
            //Memo(mno=3, memoText=Sample....3)
            //Memo(mno=4, memoText=Sample....4)
            //Memo(mno=5, memoText=Sample....5)
            //Memo(mno=6, memoText=Sample....6)
            //Memo(mno=7, memoText=Sample....7)
            //Memo(mno=8, memoText=Sample....8)
            //Memo(mno=9, memoText=Sample....9)
            //Memo(mno=10, memoText=Sample....10)
        }
    }
}
