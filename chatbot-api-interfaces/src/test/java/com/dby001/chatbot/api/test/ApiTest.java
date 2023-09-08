package com.dby001.chatbot.api.test;



import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ApiTest {

    @Test
    public void query_unanswered_questions() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.zsxq.com/v2/groups/28885155585281/topics?scope=unanswered_questions&count=20");
        get.addHeader("cookie","abtest_env=product; zsxq_access_token=9AF81B6E-236E-854D-A15D-66A9E5925FE1_670F4E2937C23809; zsxqsessionid=a7ebb7284e4950bbfc252c7074754fae");
        get.addHeader("content-type", "application/json,charset=UTF-8");

        CloseableHttpResponse response = httpClient.execute(get);

        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        }else{
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void answer() throws IOException{
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //https://api.zsxq.com/v2/groups/28885155585281/topics?scope=unanswered_questions&count=20
        HttpPost post = new HttpPost("https://api.zsxq.com/v2/topics/188518218851452/answer");
        post.addHeader("cookie","abtest_env=product; zsxq_access_token=9AF81B6E-236E-854D-A15D-66A9E5925FE1_670F4E2937C23809; zsxqsessionid=a7ebb7284e4950bbfc252c7074754fae");
        post.addHeader("content-type", "application/json,charset=UTF-8");

        String paramJson = "{\n" +
                "  \"req_data\": {\n" +
                "    \"text\": \"嗨嗨嗨\\n\",\n" +
                "    \"image_ids\": [],\n" +
                "    \"silenced\": false\n" +
                "  }\n" +
                "}";

        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("text/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);

        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        }else{
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }
}
