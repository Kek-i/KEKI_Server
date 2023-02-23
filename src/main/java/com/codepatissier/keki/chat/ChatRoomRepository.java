package com.codepatissier.keki.chat;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {

    private Map<String, ChatRoom> chatRoomDTOMap;

    @PostConstruct
    private void init(){
        chatRoomDTOMap = new LinkedHashMap<>();
    }

    public List<ChatRoom> findAllRooms(){
        //채팅방 생성 순서 최근 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRoomDTOMap.values());
        Collections.reverse(result);

        return result;
    }

    public ChatRoom findRoomById(String id){
        return chatRoomDTOMap.get(id);
    }

    public ChatRoom createChatRoomDTO(String name){
        ChatRoom room = ChatRoom.create(name);
        chatRoomDTOMap.put(room.getRoomId(), room);

        return room;
    }
}