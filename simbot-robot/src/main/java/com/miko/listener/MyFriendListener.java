package com.miko.listener;

import love.forte.di.annotation.Beans;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.action.ReplySupport;
import love.forte.simbot.definition.Friend;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.FriendMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 好友相关事件监听器。
 *
 * @author DengXueQian
 * @version v1.0
 * @createTime 2022/5/18
 */
@Beans
public class MyFriendListener {

    private static final Logger LOGGER = LoggerFactory.getLogger("好友消息");

    /**
     * 监听好友消息，并且回复这个好友一句"是的"。
     *
     * @param event 监听的事件对象
     * @return
     */
    @Listener
    public void friendListen(FriendMessageEvent event) {
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
    }
}