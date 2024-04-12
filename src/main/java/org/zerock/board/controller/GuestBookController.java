package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.GuestBookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.GuestBookService;

@Controller
@RequestMapping("/guestbook") //http://localhost/guestbook/???
@Log4j2
@RequiredArgsConstructor // 자동 주입
public class GuestBookController {

    private final GuestBookService service; // final로 선언
    //---------------------------목록----------------------------
    @GetMapping("/")
    public String index() {
        return "redirect:/guestbook/list";
    }

    @GetMapping( "/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("GuestBookController.list 메서드 실행" + pageRequestDTO);
        model.addAttribute("result", service.getList(pageRequestDTO)); // 페이징에 관한 메서드 값 생성
    }
    //---------------------------등록----------------------------
    @GetMapping("/register")
    public void register(){
        log.info("register get...");

    }

    @PostMapping("/register")
    public String registerPost(GuestBookDTO dto, RedirectAttributes rttr) {
        log.info("dto..." + dto);

        Long gno = service.register(dto);

        rttr.addFlashAttribute("msg", gno); // 한 번만 msg라는 변수를 사용(모달)

        return "redirect:/guestbook/list"; // 처리 후 목록 화면으로 이동
    }

    //---------------------------조회 및 수정----------------------------
    //@GetMapping("/read")
    @GetMapping({"/read", "/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {

        log.info("gno" + gno);

        GuestBookDTO dto = service.read(gno);

        model.addAttribute("dto", dto);
    }

    //---------------------------삭제----------------------------
    @PostMapping("/remove")
    public String reomve(long gno, RedirectAttributes rttr) {

        log.info("gno: " + gno);

        service.remove(gno);

        rttr.addFlashAttribute("msg", gno);

        return "redirect:/guestbook/list";
    }

    //---------------------------수정----------------------------
    @PostMapping("/modify")
    public String modify(GuestBookDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes rttr){
        // GuestBookDTO: 수정해야 하는 글의 정보를 가짐
        // PageRequestDTO: 기존의 페이지 정보를 유지함
        // RedirectAttributes: 리다이렉트로 이동하기 위해 사용함
        log.info("post modify.............");
        log.info("dto: " + dto);

        service.modify(dto);

        rttr.addAttribute("page", requestDTO.getPage());
        rttr.addAttribute("gno", requestDTO.getType());
        rttr.addAttribute("gno", requestDTO.getKeyword());
        rttr.addAttribute("gno", dto.getGno());

        return "redirect:/guestbook/read"; // 수정 작업 후 조회 페이지로 이동, 기존 페이지 정보도 같이 유지하여 조회 페이지에서 다시 목록 페이지로 이동하는데 어려움 없게 함
    }
}
