package openai.chatgptservice.chat.service;

import lombok.extern.slf4j.Slf4j;
import openai.chatgptservice.chat.dto.ChatRequestDto;
import openai.chatgptservice.chat.dto.ChatResponseDto;
import openai.chatgptservice.chat.dto.Choice;
import openai.chatgptservice.chat.dto.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenAiChatService {

    private final RestTemplate restTemplate;

    public OpenAiChatService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }
    String apiUrl = "https://api.openai.com/v1/chat/completions";

    @Value("${openai.api.key}")
    private String apiKey;

    public Choice getChatResponse(Message message){
        log.info("### getChatResponse = role: {}, content: {}", message.getRole(), message.getContent());

        //header 셋팅
        HttpHeaders headers = getHttpHeaders();

        //메시지 객체 생성 및 추가
        List<Map<String, String>> messages = new ArrayList<>();
        createMessage(message, messages);

        ChatRequestDto chatRequestDto = new ChatRequestDto("gpt-3.5-turbo", messages);

        HttpEntity<ChatRequestDto> chatRequestDtoHttpEntity = new HttpEntity<>(chatRequestDto, headers);

        ResponseEntity<ChatResponseDto> chatResponseDtoResponseEntity = restTemplate.postForEntity(apiUrl, chatRequestDtoHttpEntity, ChatResponseDto.class);

        ChatResponseDto body = chatResponseDtoResponseEntity.getBody();

        log.info("body.getId() = {}", body.getId());
        log.info("body.getObject() = {}", body.getObject());
        log.info("body.getCreated() = {}", body.getCreated());
        log.info("body.getModel() = {}", body.getModel());

        List<Choice> choices = body.getChoices();
        //log.info(choices.get(0).toString());

        return choices.get(0);
    }

    private static void createMessage(Message message, List<Map<String, String>> messages) {
        Map<String, String> messageObject = new HashMap<>();
        messageObject.put("role", message.getRole());
        messageObject.put("content", message.getContent());
        messages.add(messageObject);
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        return headers;
    }
}
