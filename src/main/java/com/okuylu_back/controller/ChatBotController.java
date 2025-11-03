package com.okuylu_back.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/public/chatbot")
@CrossOrigin(origins = "*")
public class ChatBotController {

    private final Map<String, DialogNode> dialogTree;
    private static final String START_NODE = "1";

    public ChatBotController() {
        this.dialogTree = initializeFromFile();
    }

    private Map<String, DialogNode> initializeFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("templates/dialog_tree_nested.json").getInputStream();
            return mapper.readValue(is, new TypeReference<Map<String, DialogNode>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки JSON-файла", e);
        }
    }

    @GetMapping("/start")
    public ResponseEntity<?> getStartNode() {
        DialogNode startNode = dialogTree.get(START_NODE);
        if (startNode == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Start node not found"));
        }
        return ResponseEntity.ok(startNode);
    }

    @GetMapping("/node/{nodeId}")
    public ResponseEntity<?> getNode(@PathVariable String nodeId) {
        DialogNode node = dialogTree.get(nodeId);
        if (node == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Node not found"));
        }
        return ResponseEntity.ok(node);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> processAnswer(@RequestBody AnswerRequest request) {
        DialogNode currentNode = dialogTree.get(request.getCurrentNodeId());
        if (currentNode == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid node ID"));
        }

        String nextNodeId = currentNode.answers.get(request.getSelectedAnswer());
        if (nextNodeId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Invalid answer"));
        }

        DialogNode nextNode = dialogTree.get(nextNodeId);
        if (nextNode == null) {
            DialogNode endNode = new DialogNode("Диалог завершён", Collections.emptyMap());
            return ResponseEntity.ok(endNode);
        }

        return ResponseEntity.ok(nextNode);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllNodes() {
        return ResponseEntity.ok(dialogTree);
    }

    public static class DialogNode {
        public String question;
        public Map<String, String> answers;

        public DialogNode() {}

        public DialogNode(String question, Map<String, String> answers) {
            this.question = question;
            this.answers = answers;
        }

        @Override
        public String toString() {
            return "DialogNode{question='" + question + "', answers=" + answers + '}';
        }
    }

    public static class AnswerRequest {
        private String currentNodeId;
        private String selectedAnswer;

        public String getCurrentNodeId() {
            return currentNodeId;
        }

        public void setCurrentNodeId(String currentNodeId) {
            this.currentNodeId = currentNodeId;
        }

        public String getSelectedAnswer() {
            return selectedAnswer;
        }

        public void setSelectedAnswer(String selectedAnswer) {
            this.selectedAnswer = selectedAnswer;
        }
    }
}