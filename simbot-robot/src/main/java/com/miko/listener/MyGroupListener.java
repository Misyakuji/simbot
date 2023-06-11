package com.miko.listener;

import love.forte.di.annotation.Beans;
import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simboot.filter.MatchType;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 群组相关事件监听器。
 *
 * @author DengXueQian
 * @version v1.0
 * @createTime 2022/5/18
 */
@Beans
public class MyGroupListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("群消息");

    /**消息来源群名称 */
    private String baseGroupName;
    /**消息来源群用户 */
    private String baseAuthorName;
    /**
     * 监听群组消息
     * <p>
     * 这里监听的是mirai组件所提供的特殊事件类型，
     * 一个组件的特殊事件能够提供更丰富的特性，并且更有针对性。
     * </p>
     *
     *
     * @param event 事件本体
     */
    @Listener
    @Filter(value = ".", matchType = MatchType.TEXT_STARTS_WITH)
    public void baseGroupListen(GroupMessageEvent event) {
        // 获取事件发生的群组和用户
        final String groupName = event.getGroup().getName();
        final String authorName = event.getAuthor().getUsername();

        //获取信息本体
        final Messages messages = event.getMessageContent().getMessages();

        // 在控制台打印消息内容(这里直接展示mirai的原生消息对象)。
        LOGGER.info("「{}」在「{}」里发送了消息：{}", authorName, groupName, messages);

    }

    /**
     * 处理以.开头的群消息
     */
    @Listener

    private void groupListenByStartDot(GroupMessageEvent event){
        //获取信息内容
        final String plainText = event.getMessageContent().getPlainText().trim().replace(".","");
        LOGGER.info("接受指令:"+plainText);
        event.getGroup().sendBlocking(event.toString());
    }

    /**
     * 处理bot被AT的群消息
     */
    @Listener
    @Filter(targets = @Filter.Targets(atBot = true))
    private void groupListenByAt(GroupMessageEvent event){

        //获取信息内容
        final String plainText = event.getMessageContent().getPlainText().trim();

        event.getGroup().sendBlocking(plainText);
    }

}
