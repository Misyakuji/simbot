package com.miko.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BotTaskModel{
    // 消息内容
    private String contents;
    // 发送目标
    private String targets;
    // 类型
    private String types;
}
