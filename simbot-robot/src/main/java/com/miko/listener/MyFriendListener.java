package com.miko.listener;

import love.forte.simboot.annotation.Listener;
import love.forte.simbot.action.ReplySupport;
import love.forte.simbot.definition.Friend;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.FriendMessageEvent;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 好友相关事件监听器。
 *
 * @author DengXueQian
 * @version v1.0
 * @createTime 2022/5/18
 */
@Component
public class MyFriendListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyFriendListener.class);

    /**
     * 监听好友消息，并且回复这个好友一句"是的"。
     *
     * @param event 监听的事件对象
     * @return
     */
//    @Listener
    public EventResult friendListen(FriendMessageEvent event) {
        //从事件中获取好友对象
        final Friend currentFriend = event.getFriend();

        //输出日志
        LOGGER.info("friend: {}({}), message: {}",
                currentFriend.getUsername(),
                currentFriend.getId(),
                event.getMessageContent().getPlainText());

        //获取内容
        final String plainText = event.getMessageContent().getPlainText().trim();

        //先判断 event 事件对象是否允许"回复"，在允许的情况使用"reply(reply)", 不允许则通过获取好友来直接发送消息。
        if (event instanceof ReplySupport) {
            //
            ((ReplySupport) event).replyBlocking(plainText);

        } else {

            currentFriend.sendBlocking(plainText);
        }


        // 返回 EventResult.truncate 代表阻止后续其他监听函数的执行。
        //  return EventResult.truncate();
        return EventResult.defaults();
    }

    /**
     * 监听消息
     */
    @Listener
    public EventResult onMessage(FriendMessageEvent event) {
        String msg = event.getMessageContent().getPlainText();
        //把空格自动转换为逗号
        msg = msg.trim().replaceAll(" ", ",");
        LOGGER.info(event.getFriend().getId() + "提问：" + msg);
        //AI自动回复
        String reply = AiOne(msg);
        if (reply == null) {
            reply = "宝，回复失败!重新试试把!";
        }
        //异步回复消息
        event.replyAsync(reply);
        return EventResult.defaults();
    }

    public static String AiOne(String sendMsg) {
        try {
            HttpGet httpGet = new HttpGet("http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + sendMsg);
            String user_agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.42";
            httpGet.addHeader("user-agent", user_agent);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            body = body.substring(body.indexOf("content") + 10, body.length() - 2);
            LOGGER.info("AiOne={}", body);
            return body;
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
    }
}