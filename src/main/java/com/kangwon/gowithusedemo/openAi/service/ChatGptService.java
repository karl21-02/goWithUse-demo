package com.kangwon.gowithusedemo.openAi.service;

import com.kangwon.gowithusedemo.openAi.dto.response.QuestionRequestDto;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {

    private final String openaiApiKey = "sk-rHI9w1v22n5Vz0lB23jQjp4HW9ViLmrQNDOaCunXhpT3BlbkFJaEXW0rSpTrFvOknuyTzTwbrTj5fJpvrTfQ_pAJpBoA";

    public String callOpenAiApi(String content) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.openai.com/v1/chat/completions";

        // JSON 객체 생성
        JSONObject message = new JSONObject();
        message.put("role", "system");
        message.put("content","You are now an AI assistant that recommends locations based on various information provided by the user. "
                + "The users may be multiple individuals, and you must use both previously learned data and new data to recommend the optimal places that can satisfy multiple users. "
                + "You will be provided with category information and user data that you need to analyze. Based on this analysis, you are expected to provide recommendations. "
                + "The information will include: 1. The area where the user lives, 2. The area the user wants to experience, 3. The time the user wants to experience, 4. Information about the companions, and 5. What the user wants to experience.\n\n"
                + "Your responses must be structured in a four-step format, as follows:\n\n"
                + "1. From [Starting Location] to [Destination]\n"
                + "   - Route: Provide a detailed route, including the estimated travel time and any relevant travel information.\n\n"
                + "2. [Activity] at [Location Name]\n"
                + "   - Address: Provide the full address of the location.\n"
                + "   - Time: Indicate the time when the activity can take place.\n"
                + "   - Features: Describe key features of the location, including atmosphere and available options.\n\n"
                + "3. [Relaxation or Activity] at [Location Name]\n"
                + "   - Address: Provide the full address of the location.\n"
                + "   - Activity: Describe the relaxation or activity available at this location, highlighting its unique qualities.\n\n"
                + "4. [Final Activity] at [Location Name]\n"
                + "   - Address: Provide the full address of the location.\n"
                + "   - Activity: Describe the final activity, ideally something to conclude the trip, such as enjoying local specialties.\n\n"
                + "Your responses must include the following details for each location: the name of the location, the number of views by other users on our platform, the number of comments left by users about the location, and the number of likes the location has received. "
                + "Additionally, each location must be categorized. The categories include but are not limited to: Local, Chain, Food, Café, Restaurant, Healing, Relax, Romantic, Scenic, Cozy, Modern, Traditional, Family-friendly, Pet-friendly, Quick bites, Fine dining, Organic, Vegan, Vegetarian, Casual, Elegant, Trendy, Classic, Hidden gems, Outdoor seating, Indoor seating, Waterfront, Mountain view, Historical, and Cultural. "
                + "Provide all responses in only Vietnamese language and not language. Keep your answers concise and simple.");
        // 유저의 질문을 가져와서 JSON 객체 생성
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", content);  // 유저의 질문을 추가


        JSONArray messagesArray = new JSONArray();
        messagesArray.put(message);
        messagesArray.put(userMessage);  // 유저 메시지를 배열에 추가

        JSONObject json = new JSONObject();
        json.put("model", "gpt-4-turbo");
        json.put("messages", messagesArray);
        json.put("max_tokens", 2000);
        json.put("temperature", 0.3);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openaiApiKey);

        HttpEntity<String> entity = new HttpEntity<>(json.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                // 전체 응답을 출력하여 확인
                System.out.println("API 응답: " + response.getBody());

                JSONObject responseJson = new JSONObject(response.getBody());

                // 응답에 "choices" 배열이 있는지 확인
                if (responseJson.has("choices")) {
                    JSONArray choicesArray = responseJson.getJSONArray("choices");

                    // "choices" 배열이 비어있는지 확인
                    if (choicesArray.length() > 0) {
                        // 첫 번째 choice에서 "message" 객체를 가져옴
                        JSONObject choice = choicesArray.getJSONObject(0);
                        JSONObject messageObject = choice.getJSONObject("message");

                        // "message" 객체에서 "content" 키를 가져옴
                        if (messageObject.has("content")) {
                            return messageObject.getString("content");
                        } else {
                            return "API 응답의 'message' 객체에 'content' 키가 없습니다.";
                        }
                    } else {
                        return "API 응답의 'choices' 배열이 비어 있습니다.";
                    }
                } else {
                    return "API 응답에 'choices' 배열이 없습니다.";
                }
            } else {
                throw new RuntimeException("API 요청 실패: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("HTTP 요청 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (RestClientException e) {
            throw new RuntimeException("RestTemplate 에러: " + e.getMessage(), e);
        }
    }
}
