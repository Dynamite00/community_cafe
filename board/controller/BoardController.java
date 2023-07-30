package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")    // localhost:8090/board/write 주소로 접소을 하면 "boardwrite 리턴 값을 반환해서 보여줌
    public String boardwriteForm() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model) {  // MultipartFile file(매개변수) 과 boardwrite.html의 input의 name과 일치시켜줘야함

       //system.out.println(board.getTitle());   // entity 내 Lombok @DATA 노테이션 작성하여 board.get() 추가

        boardService.write(board);   // 상단에 @Autowired, private 로 주입

        model.addAttribute("message", "게시물 작성이 완료되었습니다.");    // model 에 담겨서 message.html 으로 넘겨줌
        model.addAttribute("searchUrl", "/board/list");


        return "message";

    }

    @GetMapping("/board/list")
        public String boardList(Model model,
                                @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,   // 다시 넘겨줄 때 매개변수에 Model을 적어줌
                                String searchKeyword){

        Page<Board> list = null;    // Page 클래스, Board 제네릭 으로 list 선언해줌

        if(searchKeyword == null){
            list = boardService.boardList(pageable);        // 검색하는 단어가 들어오지 않았을 때 처리
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);   // 검색하는 단어가 들어왔을 때 처리
        }


        int nowPage = list.getPageable().getPageNumber() + 1;        // pageable 에서 넘어온 현재 페이지를 불러옴, +1 해주는 이유는 pageable이 가지고 있는 페이는 0에서 시작하기 때문에 눈에 보이는 부분보다 1이 적음
        int startPage = Math.max(nowPage - 4, 1);                    //  Math.max(a,b) 를 사용하여 a,b 둘 중 가장 큰 숫자를 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages());   // total 페이지를 넘어가버리면 total 페이지를 반환하도록 list.getTotalPages() 작성

        model.addAttribute("list", list);     //  boardService.boardList() 를 실행하면 반환되는 list를 List 라는 이름으로 넘김

        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        return "boardlist";
    }

    @GetMapping("board/view")   //localhost:8090/board/view?id=1
    public String boardView(Model model, Integer id){

        model.addAttribute("board", boardService.boardView(id));   // 넘겨주고 출력은 처리해주지 않았으니 thymeleaf 에서 처리 해줘야함 (boardview.html 에서 thymeleaf 문법 추가)

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model){   // /board/delete 주소에 들어왔을 때, id 값을 찾아내서 넘어온 id 값을 메서드에 담아서 서비스에 보내준 후 삭제 처리

        boardService.boardDelete(id);

        model.addAttribute("message", "게시물 삭제가 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";


        // return "redirect:/board/list"; -> 게시물 삭제 후 list 주소로 돌아오게 해줌
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify (@PathVariable("id") Integer id, Model model){    // /board/modify/{id} 주소가 들어왔을 때 뒤에 {id}가 @PathVariable 에 인식이 돼서 Integer 형태의 id로 들어옴
                                                                   // view?id=1 의 쿼리 스트링에서 @PathVariable 를 사용하면 view/1 이런 식으로 깔끔하게 URL이 생성됨
        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
        public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model) {   // Board board : 기존에 있던 내용 가져옴

        Board boardTemp = boardService.boardView(id);    // 기존에 있던 제목과 내용
        boardTemp.setTitle(board.getTitle());            // 새로 입력한 내용을 가지고 기존에 있던 제목과 내용에 덮어씌움
        boardTemp.setContent(board.getContent());        // JPA에는 덮어씌우는 것이 아니라 변경 감지(Dirty Checking)이라는 기능을 사용해야함


        boardService.write(boardTemp);
        model.addAttribute("message", "게시물 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");
        return "message";


    }


}
