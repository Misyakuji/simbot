package com.miko.listener;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.ID;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 群组相关事件监听器。
 *
 * @author DengXueQian
 * @version v1.0
 * @createTime 2022/5/18
 */
@Component
public class MyGroupListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyGroupListener.class);


    @Value("${simbot.default-notice-groups}")
    private String groupId;

    /**
     * 监听群组消息
     * <p>
     * 这里监听的是mirai组件所提供的特殊事件类型，
     * 一个组件的特殊事件能够提供更丰富的特性，并且更有针对性。
     * </p>
     *
     * @param event 事件本体
     */
    @Listener
    public EventResult baseGroupListen(GroupMessageEvent event) {
        // 获取事件发生的群组和用户
        final String groupName = event.getGroup().getName();
        final String authorName = event.getAuthor().getUsername();
        final String groupId = event.getGroup().getId().toString();
        //获取信息本体
        final Messages messages = event.getMessageContent().getMessages();

        // 在控制台打印消息内容(这里直接展示mirai的原生消息对象)。
        LOGGER.info("「{}」在「{}」里发送了消息：{}", authorName, groupName, messages);

        if (event.getGroup().getId().compareTo(ID.$(groupId)) == 0) {
            LOGGER.info("指定群组内继续执行后续监听<" + groupId + ">");
            // 如果没有此消息没有对应的回应消息，跳过
            return EventResult.defaults();
        } else {
            LOGGER.info("指定群组外终止后续监听<" + groupId + ">");
            // 返回 EventResult.truncate 代表阻止后续其他监听函数的执行。
            return EventResult.truncate();
        }
    }

    /**
     * 处理以.开头的群消息
     */
    @Listener
    @Filter(value = ".", matchType = MatchType.TEXT_STARTS_WITH)
    public EventResult groupListenByStartDot(GroupMessageEvent event) {
        final String plainText = event.getMessageContent().getPlainText().trim().replaceFirst(".", "");
        // 撤回
        event.getMessageContent().deleteBlocking();

        event.replyAsync("<" + plainText + ">指令不存在");

        return EventResult.defaults();
    }

    /**
     * 处理bot被AT的群消息
     */
    @Listener
    @Filter(targets = @Filter.Targets(atBot = true))
    public EventResult groupListenByAt(GroupMessageEvent event) {

        String msg = event.getMessageContent().getPlainText().trim();
        // 构建回复消息
        var builder = new MessagesBuilder();
        var replyText = builder.at(event.getAuthor().getId())
                .text(msg.isBlank() ? "" : "<" + msg + ">是啥，")
                .text("康康你的~")
                .build();

        event.getGroup().sendAsync(replyText);
//        event.replyAsync(replyText);

        return EventResult.defaults();
    }
}
