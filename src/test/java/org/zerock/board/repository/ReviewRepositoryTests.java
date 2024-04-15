package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Movie;
import org.zerock.board.entity.Review;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;



    @Test
    public void insertMoviewReviews() {

        //200개의 리뷰를 등록
        IntStream.rangeClosed(1,200).forEach(i -> {

            //영화 번호
            Long mno = (long)(Math.random()*100) + 1;

            //리뷰어 번호
            Long mid  =  ((long)(Math.random()*100) + 1 );
            Member member = Member.builder().mid(mid).build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random()* 5) + 1)
                    .text("이 영화에 대한 느낌..."+i)
                    .build();

            reviewRepository.save(movieReview);
        });
    }


    @Test
    public void testGetMovieReviews() {

        Movie movie = Movie.builder().mno(92L).build();

        List<Review> result = reviewRepository.findByMovie(movie);

        result.forEach(movieReview -> {

            System.out.print(movieReview.getReviewnum());
            System.out.print("\t"+movieReview.getGrade());
            System.out.print("\t"+movieReview.getText());
            System.out.print("\t"+movieReview.getMember().getEmail());
            System.out.println("---------------------------");
        });
        //Hibernate:
        //    select
        //        r1_0.reviewnum,
        //        r1_0.grade,
        //        m1_0.mid,
        //        m1_0.email,
        //        m1_0.nickname,
        //        m1_0.pw,
        //        r1_0.moddate,
        //        r1_0.movie_mno,
        //        r1_0.regdate,
        //        r1_0.text
        //    from
        //        review r1_0
        //    left join
        //        m_member m1_0
        //            on m1_0.mid=r1_0.mmember_mid
        //    where
        //        r1_0.movie_mno=?
        //30	4	이 영화에 대한 느낌...30	r14@zerock.org---------------------------
    }
}
