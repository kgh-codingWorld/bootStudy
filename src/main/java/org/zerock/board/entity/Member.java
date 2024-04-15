package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "m_member")
public class Member extends BaseEntity{

    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    private Long mid;

    private String email;

    private String password;

    private String name;
}
