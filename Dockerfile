FROM eclipse-temurin:17.0.18_8-jdk-noble

LABEL maintainer="Livepulse <support@livepulse.com.cn>"
LABEL description="百度营销平台连接器 - 推送人群包到百度营销平台"

# 设置工作目录
WORKDIR /app

# 添加 Maven 容器（用于构建）
COPY --from=maven:3.9.6-eclipse-temurin-17 /usr/share/maven /usr/share/maven

# 设置 Maven 环境
ENV MAVEN_HOME=/usr/share/maven
ENV PATH=$MAVEN_HOME/bin:$PATH

# 复制项目文件
COPY pom.xml .
COPY src ./src

# 构建 Maven 项目
RUN mvn clean package -DskipTests -B

# 暴露端口
EXPOSE 23015

# 设置 JVM 参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/livepulse-connector-baidu-marketing-open.jar"]
