#!/bin/bash
set -x
# 修改APP_NAME为云效上的应用名
APP_NAME=f-kernel

export SPRING_PROFILES_ACTIVE=qa
PROG_NAME=$0
ACTION=$1
APP_HOME=/home/admin/${APP_NAME} # 从package.tgz中解压出来的jar包放到这个目录下
JAR_NAME=${APP_HOME}/${APP_NAME}*.jar # jar包的名字
JAVA_OUT=${APP_HOME}/logs/startup.log  #应用的启动日志
JAVA_OPTS="-server -Xms256m -Xmx512m"

# 创建出相关目录
mkdir -p ${APP_HOME}
mkdir -p ${APP_HOME}/logs
usage() {
    echo "Usage: $PROG_NAME {start|stop|restart}"
    exit 2
}

start_application() {
    echo "starting java process"
    nohup java $JAVA_OPTS -jar ${JAR_NAME} > ${JAVA_OUT} 2>&1 &
    echo "started java process"
}

stop_application() {
   checkjavapid=`ps -ef | grep java | grep ${APP_NAME} | grep -v grep |grep -v 'deploy-qa.sh'| awk '{print$2}'`

   if [[ ! $checkjavapid ]];then
      echo -e "\rno java process"
      return
   fi

   echo "stop java process"
   times=60
   for e in $(seq 60)
   do
        sleep 1
        COSTTIME=$(($times - $e ))
        checkjavapid=`ps -ef | grep java | grep ${APP_NAME} | grep -v grep |grep -v 'deploy-qa.sh'| awk '{print$2}'`
        if [[ $checkjavapid ]];then
            kill -9 $checkjavapid
            echo -e  "\r        -- stopping java lasts `expr $COSTTIME` seconds."
        else
            echo -e "\rjava process has exited"
            break;
        fi
   done
   echo ""
}
start() {
    start_application
}
stop() {
    stop_application
}
case "$ACTION" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        stop
        start
    ;;
    *)
        usage
    ;;
esac
