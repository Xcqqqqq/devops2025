package com.demo.personalAgentPlatform.module.chat.service.impl;

import com.google.gson.Gson;
import okhttp3.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * DeepSeek API 调用客户端
 */
public class DeepSeekApiClient {
    // DeepSeek API 基础地址（通用对话接口）
    private static final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    // DeepSeek API Key
    private static final String API_KEY = System.getenv("DEEPSEEK_API_KEY");
    // OkHttp客户端（全局单例）
    private final OkHttpClient okHttpClient;
    // Gson解析器
    private final Gson gson;

    // 初始化客户端（设置超时时间）
    public DeepSeekApiClient() {
        this.okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    /**
     * 带系统提示词的单轮对话调用
     * @param systemPrompt 系统提示词
     * @param userPrompt 用户提问
     * @param model 模型名称（如 deepseek-chat、deepseek-coder）
     * @return 模型回复内容
     * @throws IOException 网络/接口异常
     */
    public String chatWithSystemPrompt(String systemPrompt, String userPrompt, String model) throws IOException {
        // 构造对话消息列表（系统消息+用户消息）
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", systemPrompt));
        messages.add(new Message("user", userPrompt));

        // 构造请求体
        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel(model);
        request.setMessages(messages);
        request.setTemperature(0.7); // 随机性（0-1，越小越精准）
        request.setMaxTokens(500); // 最大回复长度

        // 发送请求并解析响应
        return sendRequest(request);
    }

    /**
     * 单轮对话调用
     * @param prompt 用户提问
     * @param model 模型名称（如 deepseek-chat、deepseek-coder）
     * @return 模型回复内容
     * @throws IOException 网络/接口异常
     */
    public String singleChat(String prompt, String model) throws IOException {
        // 构造对话消息列表（单轮仅用户消息）
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", prompt));

        // 构造请求体
        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel(model);
        request.setMessages(messages);
        request.setTemperature(0.7); // 随机性（0-1，越小越精准）
        request.setMaxTokens(2048); // 最大回复长度

        // 发送请求并解析响应
        return sendRequest(request);
    }

    /**
     * 多轮对话调用
     * @param messages 对话历史（包含用户/助手消息）
     * @param model 模型名称
     * @return 模型回复内容
     * @throws IOException 网络/接口异常
     */
    public String multiChat(List<Message> messages, String model) throws IOException {
        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel(model);
        request.setMessages(messages);
        request.setTemperature(0.7);
        request.setMaxTokens(2048);

        return sendRequest(request);
    }

    /**
     * 发送HTTP请求核心方法
     */
    private String sendRequest(DeepSeekRequest request) throws IOException {
        // 构造请求体JSON
        String requestJson = gson.toJson(request);
        RequestBody requestBody = RequestBody.create(
                requestJson,
                MediaType.parse("application/json; charset=utf-8")
        );

        // 构造HTTP请求（添加API Key认证）
        Request httpRequest = new Request.Builder()
                .url(DEEPSEEK_API_URL)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + API_KEY) // 认证头
                .addHeader("Content-Type", "application/json")
                .build();

        // 执行请求并处理响应
        try (Response response = okHttpClient.newCall(httpRequest).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("DeepSeek API请求失败: " + response.code() + " " + response.message());
            }

            // 解析响应体
            String responseJson = response.body().string();
            DeepSeekResponse deepSeekResponse = gson.fromJson(responseJson, DeepSeekResponse.class);

            // 提取助手回复内容
            if (deepSeekResponse.getChoices() != null && !deepSeekResponse.getChoices().isEmpty()) {
                return deepSeekResponse.getChoices().get(0).getMessage().getContent();
            } else {
                throw new IOException("DeepSeek API返回空响应: " + responseJson);
            }
        }
    }

    // 内部类：消息结构
    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    // 内部类：请求结构
    private static class DeepSeekRequest {
        private String model;
        private List<Message> messages;
        private double temperature;
        private int maxTokens;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public List<Message> getMessages() {
            return messages;
        }

        public void setMessages(List<Message> messages) {
            this.messages = messages;
        }

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public int getMaxTokens() {
            return maxTokens;
        }

        public void setMaxTokens(int maxTokens) {
            this.maxTokens = maxTokens;
        }
    }

    // 内部类：响应结构
    private static class DeepSeekResponse {
        private String id;
        private String object;
        private long created;
        private List<Choice> choices;
        private Usage usage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }

        public List<Choice> getChoices() {
            return choices;
        }

        public void setChoices(List<Choice> choices) {
            this.choices = choices;
        }

        public Usage getUsage() {
            return usage;
        }

        public void setUsage(Usage usage) {
            this.usage = usage;
        }
    }

    // 内部类：响应选择项
    private static class Choice {
        private int index;
        private Message message;
        private String finishReason;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public String getFinishReason() {
            return finishReason;
        }

        public void setFinishReason(String finishReason) {
            this.finishReason = finishReason;
        }
    }

    // 内部类：使用统计
    private static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;

        public int getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }
}