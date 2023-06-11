package com.miko.listener;

import jakarta.annotation.Resource;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.bot.Bot;
import love.forte.simbot.event.internal.BotStartedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * bot启动事件监听器
 *
 * @author DengXueQian
 * @version v1.0
 * @createTime 2023/2/5 20:53
 */
@Component
public class MyBotlistener {

    /**
     * bot上线提醒
     */
    @Value(value = "${simbot.master-qq}")
    private String masterId;
    @Listener
    public void botStart(BotStartedEvent event){
        event.getBot().getGroup(Identifies.ID(710117186)).sendBlocking("您的小可爱已上线~");

    }
}
