#!/bin/sh

if [ -z "$1" ]; then
	echo "请在参数中指定进程Id文件的名称！"
	exit 1
fi

CURRENT_DIR=$(cd `dirname $0`; pwd)
PROJECT_DIR=$CURRENT_DIR"/.."

# echo $PROJECT_DIR

CLASSPATH=
CLASSPATH=$CLASSPATH:$PROJECT_DIR

CLASSPATH=$CLASSPATH:$CURRENT_DIR"/../lib/*"

# echo $CLASSPATH

APPNAME=com.xgw.Bootstrap

java -Xms${app.mem} -Xmx${app.mem} -Dproject.dir=$PROJECT_DIR -classpath $CLASSPATH $APPNAME start >/dev/null 2>&1 &

if [ ! -d "${PROJECT_DIR}/pid" ]; then
	mkdir "${PROJECT_DIR}/pid"
fi

echo $! > "${PROJECT_DIR}/pid/truckShareServer-$1.pid"

echo "started"