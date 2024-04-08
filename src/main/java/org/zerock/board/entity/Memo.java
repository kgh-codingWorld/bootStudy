package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity //해당 클래스가 엔티티 역할을 담당함을 명시
@Table(name = "tbl_memo") //DB 테이블 명 지정
@ToString //객체를 문자열로 변경
@Getter
@Builder //빌더 패턴(@AllArgsConstructor, @NoArgsConstructor 필수)
@AllArgsConstructor //모든 매개값을 갖는 생성자
@NoArgsConstructor //매개값이 없는 생성자
public class Memo {
    //entity: 데이터베이스에 테이블과 필드를 생성시켜 관리하는 객체
    //엔티티를 이용하여 JPA를 활성화하려면 application.properties에 항목 추가 필수

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //strategy: 방법, 계략
                                                        //auto: JPA 구현체가 생성 방식을 결정
                                                        //IDENTITY: MariaDB용(auto increment)
                                                        //SEQUENCE: Oracle용(@SequenceGenerator와 같이 사용)
                                                        //TABLE: 키생성 전용 테이블을 생성하여 키 생성(@TableGenerator와 같이 사용)
    private Long mno;

    @Column(length = 200, nullable = false) //추가 열 설정(글자길이 200, null 허용 여부(nullable = !null))
    private String memoText;

//    create table tbl_memo (
//            mno bigint not null auto_increment,
//            memo_text varchar(200) not null,
//    primary key (mno)
//    ) engine=InnoDB
}
