package com.kangwon.gowithusedemo.openAi.controller;

import com.kangwon.gowithusedemo.openAi.dto.response.QuestionRequestDto;
import com.kangwon.gowithusedemo.openAi.service.ChatGptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/chat-gpt")
@RequiredArgsConstructor
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping("/question")
    public String sendQuestion(QuestionRequestDto requestDto, Model model) {
        String content = "place is : " + requestDto.getLocation_from() + ", place_experience is : " + requestDto.getLocation_to() + ", experience_time is : " + requestDto.getExperience_time() + ", who_with_go is : " + requestDto.getCompanion() + ", categories is : " + requestDto.getInterests() + ". so, Give me a recommendation!";
        String response = chatGptService.callOpenAiApi(content);
        model.addAttribute("response", response);
        return "result";  // `result.html` 템플릿으로 응답을 렌더링합니다.
    }
}
