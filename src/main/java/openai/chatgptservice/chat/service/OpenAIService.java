package openai.chatgptservice.chat.service;

import lombok.extern.slf4j.Slf4j;
import openai.chatgptservice.chat.dto.ChatRequestDto;
import openai.chatgptservice.chat.dto.ChatResponseDto;
import openai.chatgptservice.chat.dto.Choice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenAIService {

    private final RestTemplate restTemplate;
    String apiUrl = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAIService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String callGpt(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        List<Map<String, String>> messages = new ArrayList<>();

        // 첫 번째 메시지 객체 생성 및 추가
        Map<String, String> firstMessage = new HashMap<>();
        firstMessage.put("role", "user");
        firstMessage.put("content", "Spring에 대해서 알려줘");
        messages.add(firstMessage);

        ChatRequestDto chatRequestDto = new ChatRequestDto("gpt-3.5-turbo", messages);

        HttpEntity<ChatRequestDto> chatRequestDtoHttpEntity = new HttpEntity<>(chatRequestDto, headers);

        ResponseEntity<ChatResponseDto> chatResponseDtoResponseEntity = restTemplate.postForEntity(apiUrl, chatRequestDtoHttpEntity, ChatResponseDto.class);

        ChatResponseDto body = chatResponseDtoResponseEntity.getBody();

        log.info("body.getId() = {}", body.getId());
        log.info("body.getObject() = {}", body.getObject());
        log.info("body.getCreated() = {}", body.getCreated());
        log.info("body.getModel() = {}", body.getModel());

        String answer = "";

        List<Choice> choices = body.getChoices();
        for (Choice choice : choices) {
            answer = choice.getMessage().toString();
            log.info(choice.toString());
        }

        return answer;
    }

}
