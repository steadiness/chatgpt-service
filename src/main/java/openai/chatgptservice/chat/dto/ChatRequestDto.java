package openai.chatgptservice.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRequestDto {

    private String model;
    private List messages;

    public ChatRequestDto(String model, List messages) {
        this.model = model;
        this.messages = messages;
    }
}
