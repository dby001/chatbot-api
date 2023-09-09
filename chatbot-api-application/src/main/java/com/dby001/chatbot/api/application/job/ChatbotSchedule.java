package com.dby001.chatbot.api.application.job;

import com.alibaba.fastjson.JSON;
import com.dby001.chatbot.api.domain.ai.IOpenAI;
import com.dby001.chatbot.api.domain.zsxq.IZsxqApi;
import com.dby001.chatbot.api.domain.zsxq.model.aggregates.UnAnswerQuestionsAggregates;
import com.dby001.chatbot.api.domain.zsxq.model.vo.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;


@Configuration
@EnableScheduling
public class ChatbotSchedule{

    private Logger logger = LoggerFactory.getLogger(ChatbotSchedule.class);

//    private String groupName;
//    private String groupId;
//    private String cookie;
//    private String openAiKey;
//    private boolean silenced;
//
//    private IZsxqApi zsxqApi;
//    private IOpenAI openAI;

//    public ChatbotSchedule(String groupName, String groupId, String cookie, String openAiKey, IZsxqApi zsxqApi, IOpenAI openAI, boolean silenced) {
//        this.groupName = groupName;
//        this.groupId = groupId;
//        this.cookie = cookie;
//        this.openAiKey = openAiKey;
//        this.zsxqApi = zsxqApi;
//        this.openAI = openAI;
//        this.silenced = silenced;
//    }

    @Value("${chatbot-api.groupId}")
    private String groupId; //= "28885155585281";
    @Value("${chatbot-api.cookie}")
    private String cookie; //= "zsxq_access_token=9AF81B6E-236E-854D-A15D-66A9E5925FE1_670F4E2937C23809; abtest_env=product; zsxqsessionid=26ab6e097d2bc3f444d33d246e04d960";
    @Value("${chatbot-api.openAiKey}")
    private String openAiKey; //= "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";

    @Resource
    private IZsxqApi zsxqApi;

    @Resource
    private IOpenAI openAI;

    @Scheduled(cron = "0/10 * * * * ?")
    public void run() {
        try {
            if (new Random().nextBoolean()) {
                logger.info("随机打烊中...");
                return;
            }

            GregorianCalendar calendar = new GregorianCalendar();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 22 || hour < 7) {
                logger.info("{} 打烊时间不工作，AI 下班了！");
                return;
            }

            // 1. 检索问题
            UnAnswerQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnswerQuestionsTopicId(groupId, cookie);
            logger.info("检索结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            if (null == topics || topics.isEmpty()) {
                logger.info("{} 本次检索未查询到待会答问题");
                return;
            }

            // 2. AI 回答
            Topics topic = topics.get(topics.size() - 1);
            String answer = openAI.doChatGPT(openAiKey, topic.getQuestion().getText().trim());
            // 3. 问题回复
            boolean status = zsxqApi.answer(groupId, cookie, topic.getTopic_id(), answer, false);
            logger.info("编号：{} 问题：{} 回答：{} 状态：{}", topic.getTopic_id(), topic.getQuestion().getText(), answer, status);
        } catch (Exception e) {
            logger.error("自动回答问题异常", e);
        }
    }

}