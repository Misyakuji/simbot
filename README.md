# simbot

This is robot project with Simple Robot Framework in Springboot.

## 环境准备

- Openjdk25与maven
- MariaDB数据库
- NapCat
- 配置启动必要的环境变量
```bash
#初始化DDL
simbot-web/src/main/resources/ddl
#修改bot配置,并启动napcat的http服务与websocket服务
simbot-robot/src/main/resources/simbot-bots/default.bot.json
#deepseek与火山方舟大模型API_KEY(以linux环境为例)
export JASYPT_ENCRYPTOR_KEY="xxx"
export DEEPSEEK_API_KEY="xxx"
#JASYPT加密KEY
export ARK_API_KEY="xxx"
```

### 参考文档

```bash
#Napcat
https://napneko.github.io/guide/boot/Shell

#火山方舟大模型服务平台
https://www.volcengine.com/docs/82379/1399008?lang=zh

#deepseek开放平台
https://platform.deepseek.com/api_keys

#Spring AI
https://docs.spring.io/spring-ai/reference/api/chat/deepseek-chat.html

#Simple Robot Framework
https://simbot.forte.love/home.html
```

### 数据库

```bash
#创建数据库和用户
simbot-web/src/main/resources/ddl/init.sql

#用于创建数据库表
simbot-web/src/main/resources/ddl/schema.sql
```

### Linux环境运行

```bash

#赋予可执行权限
chmod +x mvnw

#maven打包
git pull && ./mvnw clean package -Dmaven.test.skip=true

#运行jar
java -jar simbot-starter/target/simbot-starter.jar
```

### Windows环境运行

```shell

#maven打包
./mvnw.cmd clean package '-Dmaven.test.skip=true' 

#运行jar
java -jar .\simbot-starter\target\simbot-starter.jar
```

