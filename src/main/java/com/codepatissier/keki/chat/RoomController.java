package com.codepatissier.keki.chat;

import com.codepatissier.keki.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chat")
@Log4j2
public class RoomController {

    private final ChatRoomRepository repository;

//    //채팅방 목록 조회
//    @GetMapping(value = "/rooms")
//    public ModelAndView rooms(){
//
//        log.info("# All Chat Rooms");
//        ModelAndView mv = new ModelAndView("chat/rooms");
//
//        mv.addObject("list", repository.findAllRooms());
//
//        return mv;
//    }

    //채팅방 개설
    @PostMapping(value = "/room")
    public BaseResponse<ChatRoom> create(@RequestParam String name) {

        log.info("# Create Chat Room , name: " + name);
        return new BaseResponse<>(this.repository.createChatRoomDTO(name));
    }

//    //채팅방 조회
//    @GetMapping("/room")
//    public void getRoom(String roomId, Model model){
//
//        log.info("# get Chat Room, roomID : " + roomId);
//
//        model.addAttribute("room", repository.findRoomById(roomId));
//    }
}