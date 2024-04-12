package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") // @ToString은 항상 exclude
public class Board extends BaseEntity{

    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에서 자동으로 값을 생성하도록 지정
    private Long bno;

    private String title;

    private String content;

    @ManyToOne (fetch = FetchType.LAZY)// member 쪽의 pk인 writer를 참조하여 구성해야 함(명시적으로 LAZY 로딩 지정)
    private Member writer; // 연관 관계 지정(Board와 Member는 다대일 관계가 되므로), 데이터베이스상에서 외래키의 관계로 연결된 엔티티 클래스에 설정함

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
