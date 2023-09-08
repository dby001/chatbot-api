package com.dby001.chatbot.api.test;

import com.alibaba.fastjson.JSON;
import com.dby001.chatbot.api.domain.zsxq.IZsxqApi;
import com.dby001.chatbot.api.domain.zsxq.model.aggregates.UnAnswerQuestionsAggregates;
import com.dby001.chatbot.api.domain.zsxq.model.vo.Question;
import com.dby001.chatbot.api.domain.zsxq.model.vo.Topics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRunTest {

    Logger logger = LoggerFactory.getLogger(SpringBootTest.class);

    @Value("${chatbot-api.groupId}")
    private String groupId;
    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Resource
    private IZsxqApi zsxqApi;

    @Test
    public void test_zsxqApi() throws IOException {

        UnAnswerQuestionsAggregates unAnswerQuestionsAggregates = zsxqApi.queryUnAnswerQuestionsTopicId(groupId, cookie);
        logger.info("测试结果{}", JSON.toJSONString(unAnswerQuestionsAggregates));

        List<Topics> topics = unAnswerQuestionsAggregates.getResp_data().getTopics();
        for (Topics topic : topics) {
            String topicId = topic.getTopic_id();
            String text = topic.getQuestion().getText();
            logger.info("topicID:{},text:{}", topicId, text);

            //回答问题
            boolean answer = zsxqApi.answer(groupId, cookie, topicId, text, false);
            logger.info("answer:{}", answer);
        }

    }

}
