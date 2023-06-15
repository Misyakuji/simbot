package com.miko.listener;

import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.bot.MiraiBot;
import love.forte.simbot.event.FriendAddRequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 好友事件监听器
 */
@Component
public class MyFriendEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyFriendEventListener.class);

    @Value(value = "${simbot.default-master-qq}")
    private String masterId;

    /**
     * 监听好友添加请求
     */
    @Listener
    public void onFriendAddRequest(FriendAddRequestEvent event) {
        var id = event.getFriend().getId();

        if (askMaster(event)) {
            LOGGER.info("同意添加" + id + "为好友");
            event.acceptAsync();
        } else {
            LOGGER.info("拒绝添加" + id + "为好友");
            event.rejectAsync();
        }
    }

    private boolean askMaster(FriendAddRequestEvent event){

        ID id = event.getFriend().getId();
        MiraiBot bot = (MiraiBot) event.getBot();
        //MiraiBot bot = (MiraiBot) botManager.get(event.getBot().getId());
        bot.getFriend(ID.$(masterId)).sendAsync(id + "申请添加我为好友");
//        String masterReplyMsg = event.getMessageContent().getPlainText().trim();
        return true;
    }

}