#!/bin/bash

# 颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

MAVEN_REPO_VOLUME="personal-agent-platform-maven-repo"
PROJECT_DIR="$PWD/backend"

echo -e "${GREEN}检查 Maven 缓存 Volume...${NC}"
if docker volume inspect "$MAVEN_REPO_VOLUME" &> /dev/null; then
    echo -e "${GREEN}Maven 缓存 Volume 已存在${NC}"
else
    echo -e "${GREEN}创建 Maven 缓存 Volume...${NC}"
    docker volume create "$MAVEN_REPO_VOLUME"
fi

echo -e "${GREEN}开始构建后端 JAR 包...${NC}"
docker run -it --rm --name personal-agent-platform-maven \
    -v "$MAVEN_REPO_VOLUME":/root/.m2 \
    -v "$PROJECT_DIR":/usr/src/mymaven \
    -w /usr/src/mymaven \
    maven:3.9-eclipse-temurin-17 \
    mvn clean package -DskipTests -e \
    || { echo -e "${RED}构建失败！${NC}"; exit 1; }

echo -e "${GREEN}构建成功！${NC}"
ls -lh "$PROJECT_DIR/target"/*.jar