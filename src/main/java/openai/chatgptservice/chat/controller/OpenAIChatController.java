package openai.chatgptservice.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openai.chatgptservice.chat.dto.ChatResponseDto;
import openai.chatgptservice.chat.dto.Choice;
import openai.chatgptservice.chat.dto.Message;
import openai.chatgptservice.chat.service.OpenAiChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/openai-chat/chat")
public class OpenAIChatController {

    private final OpenAiChatService openAiChatService;

    @GetMapping
    public String startChat(){
        return "chat/chat";
    }

    @PostMapping("/question")
    public ResponseEntity<Choice> sendMessage(@RequestBody Message message){
        log.info("### sendMessage = role: {}, content: {}", message.getRole(), message.getContent());
        Choice response = openAiChatService.getChatResponse(message);
        return ResponseEntity.ok(response);
    }


}
