package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")
public class Reply extends BaseEntity {

    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 데이터베이스에서 자동으로 값을 생성하도록 지정
    private Long rno;

    private String text;

    private String replyer;

    @ManyToOne
    private Board board; // 연관 관계 지정, board의 pk를 참조해야 함
}
