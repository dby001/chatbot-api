package com.dby001.chatbot.api.domain.zsxq;

import com.dby001.chatbot.api.domain.zsxq.model.aggregates.UnAnswerQuestionsAggregates;

import java.io.IOException;

/**
 * 知识星球api接口
 */
public interface IZsxqApi {

    UnAnswerQuestionsAggregates queryUnAnswerQuestionsTopicId(String groupId, String cookie) throws IOException;

    boolean answer(String groupId,String cookie,String topicId,String text,boolean silenced) throws IOException;

}
