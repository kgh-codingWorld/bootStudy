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
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        log.info("list..." + pageRequestDTO);

        model.addAttribute("result", boardService.getList(pageRequestDTO));
    }

    @PostMapping("/register")
    public String registerPost(BoardDTO dto, RedirectAttributes rttr) {

        log.info("dto..." + dto);
        // 새로 추가된 엔티티 번호
        Long bno = boardService.register(dto);

        log.info("BNO: " + bno);

        rttr.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model) {

        log.info("bno: " + bno);

        BoardDTO boardDTO = boardService.get(bno);

        log.info(boardDTO);

        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/remove")
    public String remove(long bno, RedirectAttributes rttr) {

        log.info("bno: " + bno);

        boardService.removeWithReplies(bno);

        rttr.addFlashAttribute("msg", bno);

        return "redirect:/board/list";
    }

    @PostMapping("/modiry")
    public String modify(BoardDTO dto,
                         @ModelAttribute("requestDTO") PageRequestDTO requestDTO, RedirectAttributes rttr) {

        log.info("post modify...");

        log.info("dto: " + dto);

        boardService.modify(dto);

        rttr.addAttribute("page", requestDTO.getPage());
        rttr.addFlashAttribute("type", requestDTO.getType());
        rttr.addFlashAttribute("keyword", requestDTO.getKeyword());

        rttr.addFlashAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }
}