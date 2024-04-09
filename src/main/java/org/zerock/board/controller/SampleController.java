package org.zerock.board.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.SampleDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// @RestController //기본값: JSON
@Controller
@RequestMapping("/sample") // http://localhost/sample/??? 경로가 생김
@Log4j2
public class SampleController {

    @GetMapping("/hello")
    public String[] hello(){
        return new String[]{"Hello", "world"};
    }

    @GetMapping("/ex1")
    public void ex1(){
        log.info("ex1 메서드 실행............");
        // resource/templates/sample/ex1.html에 파일 필수
    }

    @GetMapping("/ex2")
    public void model(Model model){
        //Spring은 모델 타입으로 모든 객체나 데이터를 가지고 있음
        List<SampleDTO> list = IntStream.rangeClosed(1, 20).asLongStream().mapToObj(i -> {
           SampleDTO dto = SampleDTO.builder()
                   .sno(i)
                   .first("첫 번째 field..."+i)
                   .last("마지막 field..."+i)
                   .regTime(LocalDateTime.now())
                   .build(); // 빌더 패턴을 이용하여 값을 리스트로 만듦
            return dto;
        }).collect(Collectors.toList()); // 문법임 외워라

        model.addAttribute("list", list); // model에 한 개의 객체를 담음
        // 프론트에서 list를 호출하면 list 객체가 나옴

        log.info("ex2 메서드 실행............");
        // resource/templates/sample/ex2.html에 파일 필수
    }

    @GetMapping({"/exInline"}) // 주로 js 처리에 유용
    public String exInline(RedirectAttributes rttr) {
        log.info("exInline......");

        SampleDTO dto = SampleDTO.builder()
                .sno(100L)
                .first("First..100")
                .last("Last..100")
                .regTime(LocalDateTime.now())
                .build();
        rttr.addFlashAttribute("result", "success");
        rttr.addFlashAttribute("dto", dto);

        return "redirect:/sample/ex3";
    }

    @GetMapping("/ex3")
    public void ex3() {
        log.info("ex3");
    }

    @GetMapping({"/ex2", "/exLink"})
    public void exModel(Model model) {
        List<SampleDTO> list = IntStream.rangeClosed(1,20).asLongStream().mapToObj(i -> {
            SampleDTO dto = SampleDTO.builder()
                    .sno(i)
                    .first("first.."+i)
                    .last("last.."+i)
                    .regTime(LocalDateTime.now())
                    .build();
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("list", list);
    }

    @GetMapping({"/exLayout1", "/exLayout2", "/exTemplate", "exSidebar"})
    public void exLayout1(){
        log.info("exLayout...");
    }
}
