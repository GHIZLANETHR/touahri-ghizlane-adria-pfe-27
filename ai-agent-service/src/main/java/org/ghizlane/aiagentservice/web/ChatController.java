package src.main.java.org.ghizlane.aiagentservice.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatClient chatClient;
    private final ToolCallbackProvider tools;

    public ChatController(
            ChatClient.Builder builder,
            ToolCallbackProvider tools
    ) {
        this.chatClient = builder
                .defaultSystem("""
                        You are a banking assistant.
                        Always consult the available tools
                        before answering questions about
                        accounts or transactions.
                        Never invent banking data.
                        """)
                .build();

        this.tools = tools;
    }

    @PostMapping
    public Map<String, String> chat(
            @RequestBody Map<String, String> request
    ) {
        String response = chatClient
                .prompt(request.get("message"))
                .tools(tools)
                .call()
                .content();

        return Map.of("response", response);
    }
}