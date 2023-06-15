package com.miko.quartz;

import love.forte.simbot.Identifies;
import love.forte.simbot.application.BotManagers;
import love.forte.simbot.bot.BotManager;
import love.forte.simbot.component.mirai.MiraiFriend;
import love.forte.simbot.component.mirai.bot.MiraiBot;
import love.forte.simbot.component.mirai.bot.MiraiBotManager;
import love.forte.simbot.component.mirai.message.MiraiSendOnlyImage;
import love.forte.simbot.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Bot定时任务
 */
@Component
public class BotScheduledTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(BotScheduledTask.class);
    @Autowired
    private BotManagers botManagers;

    @Value("${simbot.default-master-qq}")
    private List<String> qqSet;

    /**
     * 定义语录
     */
    static List<String> content;

    /**
     * 定义图片
     */
    static List<String> images;

    static {
        content = new ArrayList<>();
        images = new ArrayList<>();
        LOGGER.info("开始加载定义语录~~~");
        // 定义语录
        content.add("遇见你之前，我没想过结婚，遇见你之后，结婚我没想过别人。");
        content.add("你走向我，我觉得一日不见如隔三秋，你朝我笑，我又觉得三秋未见不过一日。");
        content.add("如果可以和你在一起，我宁愿让天空所有的星光全部损落，因为你的眼睛，是我生命里最亮的光芒。");
        content.add("我一直喜欢温暖的东西，而世界上最温暖的，无非阳光和你。");
        content.add("我不要短暂的温存，只要你一世的陪伴。");
        content.add("我没有特别喜欢的零食，没有特别喜欢的电影，没有特别喜欢听的歌，但我就是特别喜欢你。");
        content.add("一年四季想陪你度过，世间琐事都想与你做，此生也只想同你尝尽烟火。");
        content.add("我还是很喜欢你，像七月的风和八月的雨，毫无交集。");
        content.add("你在我身边也好，在天边也罢，想到世界的角落有一个你，觉得整个世界也变得温柔安定了。");
        content.add("我的人生理念是活十成，一成不变的是爱你，剩下九成是加倍爱你。");
        LOGGER.info("开始加载表情图片~~~");
        // 表情图片
        images.add("https://raw.githubusercontent.com/Misyakuji/wallpaper/master/100311225_p0.jpg");
        images.add("https://raw.githubusercontent.com/Misyakuji/wallpaper/master/38676711.png");
    }

    /**
     * 每一小时发送一次: 0 0 0/1 * * ?
     * 每五分钟发送一次: 0 0/5 * * * ?
     * 每天晚上8点: 0 0 20 * * ?
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void handler() {
        Calendar calendar = Calendar.getInstance();
        // 获取当前小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // 只在早上8点到晚上22点发送消息
        if (hour < 8 || hour > 22) {
            return;
        }
        //发送QQ私信
        qqSet.forEach(qq -> {
            try {
                final String msg = content.get(new SecureRandom().nextInt(content.size()));
                final String img = images.get(new SecureRandom().nextInt(images.size()));
                // 获取所有的bot
                for (BotManager<?> manager : botManagers) {
                    if (manager instanceof MiraiBotManager miraiBotManager) {
                        MiraiBot bot = miraiBotManager.get(Identifies.ID("2810013347"));
                        // 拿到bot，怎么操作看你心情，比如往某个群发消息
                        assert bot != null;
                        MiraiFriend friend = bot.getFriend(Identifies.ID(qq));
                        assert friend != null;
                        friend.sendAsync(msg);
                        // 构建图片
                        MiraiSendOnlyImage offlineImage = MiraiSendOnlyImage.of(Resource.of(new URL(img)));
                        // 上传图片到friend，然后向其发送
                        // 得到发送回执的 Future
                        offlineImage.uploadAsync(friend).thenCompose(friend::sendAsync);
                        LOGGER.info("正在发送定义语录，当前qq={}, 语录={}, img={}", qq, msg, img);
                        break;
                    }
                }
            } catch (Exception e) {
                LOGGER.error("发送定义语录异常, qq={}", qq, e);
            }
        });
    }
}