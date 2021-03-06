#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java

export SPARK_HOME=/opt/spark
export SPARK_CONF_DIR=$SPARK_HOME/conf
export SPARK_LIB_DIR=$SPARK_HOME/lib
export SPARK_ASSEMBLY_JAR=$SPARK_LIB_DIR/"$(ls -1 "$SPARK_LIB_DIR" | grep "^spark-assembly.*hadoop.*\.jar$" || true)"
export SPARK_DATAUNCLEUS_CLASSPATH=`ls -l $SPARK_LIB_DIR | grep datanucleus | awk 'BEGIN{ORS=":"}{print "'$SPARK_LIB_DIR'/"$NF}'`
. $SPARK_CONF_DIR/spark-env.sh

export HIVE_HOME=/opt/datadir
export HIVE_CONF_DIR=$HIVE_HOME/conf
export HIVE_AUXLIB_DIR=$HIVE_HOME/auxlib
for jar in $HIVE_AUXLIB_DIR/*.jar; do
  HIVE_AUXLIB_CLASSPATH=$HIVE_AUXLIB_CLASSPATH:$jar
done
export HIVE_AUXLIB_CLASSPATH=$HIVE_AUXLIB_CLASSPATH

export PRESTO_COODINATOR_URI=coordinator01.presto01.sjs.ted:8080
export PRESTO_DEFAULT_CATALOG=hive
export PRESTO_DEFAULT_SCHEMA=default

export HBASE_HOME=/etc/hbase
export HBASE_CONF_DIR=$HBASE_HOME/conf
export HBASE_ZOOKEER_QUORUM=`cat $HBASE_CONF_DIR/hbase-site.xml | grep -v "<\!--" | grep hbase.zookeeper.quorum -A 1 | tail -n 1 | awk -F"<value>" '{print $2}' | awk -F"</value>" '{print $1}'`

export PHOENIX_HOME=/opt/phoenix
export PHOENIX_LIB_DIR=$PHOENIX_HOME/lib
export PHOENIX_VERSION=`ls -l $PHOENIX_HOME/phoenix-*-server.jar | awk -F"phoenix-" '{print $NF}' | awk -F"-server.jar" '{print $1}'`
export PHOENIX_SERVER_JAR=$PHOENIX_HOME/phoenix-${PHOENIX_VERSION}-server.jar
export PHOENIX_SPARK_JAR=$PHOENIX_LIB_DIR/phoenix-spark-${PHOENIX_VERSION}.jar

export BIGDATAKIT_HOME=/opt/bigdatakit
export BIGDATAKIT_CONF_DIR=$BIGDATAKIT_HOME/conf
export BIGDATAKIT_LIB_DIR=$BIGDATAKIT_HOME/lib
for jar in $BIGDATAKIT_LIB_DIR/*.jar; do
  BIGDATAKIT_CLASSPATH=$BIGDATAKIT_CLASSPATH:$jar
done
export BIGDATAKIT_CLASSPATH=$BIGDATAKIT_CLASSPATH
export BIGDATAKIT_VERSION=`ls -l $BIGDATAKIT_LIB_DIR/bigdatakit-sdk-*.jar | awk -F"sdk-" '{print $NF}' | awk -F".jar" '{print $1}'`

export BIGDATAKIT_HDFS_HOME=/user/spark/bigdatakit
export BIGDATAKIT_HDFS_PACKAGE_DIR=$BIGDATAKIT_HDFS_HOME/packages
export BIGDATAKIT_LOCAL_HOME=~/.bigdatakit
export BIGDATAKIT_LOCAL_PACKAGE_DIR=$BIGDATAKIT_LOCAL_HOME/packages

export BIGDATAKIT_STARTUP_GIT_URL=http://gitlab.dev.sogou-inc.com/sogou-spark/bigdatakit-startup.git
