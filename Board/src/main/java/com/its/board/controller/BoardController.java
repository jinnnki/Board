package com.its.board.controller;

import ch.qos.logback.classic.spi.EventArgUtil;
import com.its.board.domain.entity.Board;
import com.its.board.dto.BoardDto;
import com.its.board.service.BoardService;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


//TODO 1.Table 아이디가 Run을 다시 할때마다 50정도씩 기본 값이 올라가는데 이유?
//TODO 2.BoardApplication(Main)에 @bean을 넣어야 put,deledemapping이 작동하는데 이유를 모르겠다 (인터넷보고 코드 짜맞춘거라)
//TODO 3. 검색기능 사용시 페이징이 풀린다 이동하는 주소 확인해보자


@Controller
public class BoardController {
    private BoardService boardService;

    public BoardController(BoardService boardService) {

        this.boardService=boardService;
    }
    //페이징
    @GetMapping("/")
    public String list(Model model, @RequestParam(value = "page", defaultValue ="1") Integer pageNum) {
        List<BoardDto> boardDtoList = boardService.getBoardlist(pageNum);
        Integer[] pageList = boardService.getPageList(pageNum);

        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("pageList", pageList);
        return "board/list";
    }
//    @GetMapping("/") //기존
//    public String list(Model model) {
//        List<BoardDto> boardDtoList =boardService.getBoardlist();
//        model.addAttribute("boardList", boardDtoList);
//        return "board/list";
//    }

    @GetMapping("/post")
    public String write() {
        return "board/write";
    }

    @PostMapping ("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }

    @GetMapping("/post/{no}") //디테일페이지
    public String detail(@PathVariable("no") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/detail";
    }

    @GetMapping("/post/edit/{no}") //수정
    public String edit(@PathVariable("no") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);

        model.addAttribute("boardDto", boardDto);
        return "board/update";
    }

    @PutMapping("/post/edit/{no}") //수정한거 새로저장
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }
    //게시글 삭제
    @DeleteMapping("/post/{no}")
        public String delete(@PathVariable("no")Long id) {
            boardService.deletePost(id);

            return "redirect:/";
        }
    //게시글 검색
    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword);
        model.addAttribute("boardList",boardDtoList);
        return "board/list";
    }

}
