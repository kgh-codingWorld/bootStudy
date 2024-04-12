package org.zerock.board.repository.search;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository{

    public SearchBoardRepositoryImpl() { //QuerydslRepositorySupport를 상속 받았으므로 super()를 이용해 호출해야 함
        super(Board.class);
    }
    @Override
    public Board search1() {

        log.info("search1...");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        tuple.groupBy(board);

        //jpqlQuery.select(board).where(board.bno.eq(1L));

        log.info("---------------------------------");
        //log.info(jpqlQuery);
        log.info(tuple);
        log.info("---------------------------------");

        //List<Board> result = jpqlQuery.fetch();
        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }
}
