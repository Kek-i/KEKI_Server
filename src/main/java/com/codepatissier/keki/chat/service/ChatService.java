package com.codepatissier.keki.chat.service;

import com.codepatissier.keki.chat.dto.ChatMessage;
import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.user.entity.User;
import com.codepatissier.keki.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.codepatissier.keki.common.BaseResponseStatus.INVALID_USER_IDX;
import static com.codepatissier.keki.common.Constant.ACTIVE_STATUS;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;

    public void enter(ChatMessage message) throws BaseException {
        User user = userRepository.findByUserIdxAndStatusEquals(message.getUserIdx(), ACTIVE_STATUS).orElseThrow(() -> new BaseException(INVALID_USER_IDX));
        message.setMessage(user.getNickname() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/subscribe/rooms/" + message.getChatRoomId(), message.getMessage());
    }
}
