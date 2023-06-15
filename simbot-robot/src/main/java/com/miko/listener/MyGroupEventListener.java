package com.miko.listener;


import love.forte.simboot.annotation.Listener;
import love.forte.simbot.component.mirai.MiraiGroup;
import love.forte.simbot.component.mirai.MiraiMember;
import love.forte.simbot.component.mirai.event.MiraiGroupNudgeEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinEvent;
import love.forte.simbot.component.mirai.event.MiraiMemberLeaveEvent;
import love.forte.simbot.event.EventResult;
import love.forte.simbot.event.GroupJoinRequestEvent;
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
public class MyGroupEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyGroupEventListener.class);

    @Value("${simbot.default-notice-groups}")
    private String groupId;

    /**
     * 入群申请事件
     * @param event
     * @return EventResult
     */
    @Listener
    private EventResult groupJoinRequestListen(GroupJoinRequestEvent event){
        LOGGER.info("MiraiMemberJoinRequestEvent>>"+event);
        event.getBot().getGroup(event.getGroup().getId()).sendAsync("入群申请事件");
        return EventResult.defaults();
    }

    /**
     * 成员入群事件
     * @param event
     * @return EventResult
     */
    @Listener
    private EventResult memberJoinListen(MiraiMemberJoinEvent event){
        LOGGER.info("MiraiMemberJoinEvent>>"+event);
        final MiraiGroup group = event.getGroup();
        MiraiMember member = event.getMember();
        /*
         * 获取消息构建器
         */
        var builder = new MessagesBuilder();

        var text = builder.at(member.getId())
                .text("呐呐，欢迎")
                .text(member.getNickname())
                .text("加入本聊,请查看群公告")
                .text("你大概是第")
                .text(String.valueOf(group.getCurrentMember() - 1))
                .text("个加入本群的")
                .build();

        group.sendAsync(text);
        return EventResult.defaults();
    }

    /**
     * 成员退群事件
     * @param event
     * @return EventResult
     */
    @Listener
    private EventResult memberLeaveListen(MiraiMemberLeaveEvent event){
        LOGGER.info("MiraiMemberLeaveEvent>>"+event);
        final MiraiGroup group = event.getGroup();
        String memberId = event.getMember().getId().toString();

        /*
         * 获取消息构建器
         */
        var builder = new MessagesBuilder();

        var text = builder.text(memberId)
                .text("走的很安详~")
                .build();

        group.sendAsync(text);
        return EventResult.defaults();
    }

    /**
     * 戳一戳
     * @param event
     * @return EventResult
     */
    @Listener
    private EventResult nudgeListen(MiraiGroupNudgeEvent event) {

//        event.replyAsync("不许戳我");
        event.getGroup().sendAsync("不许戳我");
        return EventResult.defaults();
    }
}
