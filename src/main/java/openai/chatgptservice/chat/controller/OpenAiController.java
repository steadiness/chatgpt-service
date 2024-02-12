package openai.chatgptservice.chat.controller;

import lombok.RequiredArgsConstructor;
import openai.chatgptservice.chat.dto.ChatResponseDto;
import openai.chatgptservice.chat.service.OpenAIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/chat-gpt")
@RequiredArgsConstructor
public class OpenAiController {

    private final OpenAIService openAIService;

    @GetMapping("/question")
    public String callGPT(){
        return openAIService.callGpt();
    }

}
