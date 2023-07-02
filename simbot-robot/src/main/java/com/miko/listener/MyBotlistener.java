package com.miko.listener;


import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.component.mirai.bot.MiraiBot;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.internal.BotStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * bot启动事件监听器
 *
 * @author DengXueQian
 * @version v1.0
 * @createTime 2023/2/5 20:53
 */
@Component
public class MyBotlistener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyBotlistener.class);

    @Value(value = "${simbot.default-master-qq}")
    private String masterId;

    @Value("${simbot.default-notice-groups}")
    private List<String> groups;

    @Value("${simbot.default-login-msg}")
    private String loginMsg;

    /**
     * bot上线提醒
     * @param event
     * @return EventResult
     */
    @Listener
    public EventResult botStart(BotStartedEvent event) {
        // 获取bot本体
        var bot = (MiraiBot) event.getBot();

        // 向master和群组发送上线通知
        bot.getFriend(ID.$(masterId)).sendAsync(loginMsg);
        groups.forEach(id -> bot.getGroup(Identifies.ID(id)).sendAsync(loginMsg));
        LOGGER.info("[bot<{}>上线],{}", bot.getId(), event.toString());
        return EventResult.defaults();
    }
}
