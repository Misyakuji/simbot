package com.miko.listener;

import com.alibaba.fastjson2.JSONObject;
import com.miko.util.ApiUtil;
import lombok.val;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.action.ReplySupport;
import love.forte.simbot.definition.Friend;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.FriendMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;


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
     * 监听好友消息:链接类型，返回链接内容
     *
     * @param event 监听的事件对象
     * @return
     */
    @Listener
    @Filter(value = "http", matchType = MatchType.TEXT_STARTS_WITH)
    public EventResult onMessageCall(FriendMessageEvent event) {
        String msg = event.getMessageContent().getPlainText();
        try {
            val reply = ApiUtil.callGet(msg);
            event.replyAsync(reply);
        } catch (IOException e) {
            event.replyAsync("IOException");
        } catch (Exception e){
            event.replyAsync("获取链接失败");
        }
        //异步回复消息
        return EventResult.truncate();
    }
    /**
     * 监听消息
     */
    @Listener
    public EventResult onMessage(FriendMessageEvent event) {
        String msg = event.getMessageContent().getPlainText();
        if (msg.contains("http")) {
            return EventResult.defaults();
        }
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
        String baseUrl = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=";
        try {
            String url = baseUrl + sendMsg;
            val result = ApiUtil.callGet(url);
            LOGGER.info("AiOne={}", result);
            val resultMap = JSONObject.parseObject(result.toString(), HashMap.class);
            return resultMap.get("content").toString();
        } catch (Exception e) {
            LOGGER.error(e.toString());
            return null;
        }
    }
}