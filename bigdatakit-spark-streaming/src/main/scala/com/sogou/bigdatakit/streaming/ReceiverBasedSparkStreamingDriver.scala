package com.sogou.bigdatakit.streaming

import com.sogou.bigdatakit.common.util.CommonUtils._
import com.sogou.bigdatakit.kafka.serializer.AvroFlumeEventBodyDecoder
import com.sogou.bigdatakit.streaming.processor.{DStreamProcessor, LineProcessor, RDDProcessor}
import kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.LoggerFactory

/**
  * Created by Tao Li on 2015/8/19.
  */
class ReceiverBasedSparkStreamingDriver(settings: SparkStreamingSettings)
  extends SparkStreamingDriver with Serializable {
  private val LOG = LoggerFactory.getLogger(getClass)

  private val batchDuration = Seconds(settings.BATCH_DURATION_SECONDS)
  private val kafkaParams = Map[String, String](
    "zookeeper.connect" -> settings.KAFKA_ZOOKEEPER_QUORUM,
    "group.id" -> settings.KAFKA_CONSUMER_GROUP
  ) ++ settings.kafkaConfigMap
  private val topicMap = settings.KAFKA_TOPICS.split(",").
    map((_, settings.KAFKA_CONSUMER_THREAD_NUM)).toMap

  private val processor = Class.forName(settings.PROCESSOR_CLASS)
    .newInstance.asInstanceOf[DStreamProcessor]

  @transient private var sscOpt: Option[StreamingContext] = None

  def start = {
    val conf = new SparkConf()
    for ((k, v) <- settings.sparkConfigMap) conf.set(k, v)
    conf.setAppName(settings.SPARK_APP_NAME).setMaster(settings.SPARK_MASTER_URL).
      set("spark.scheduler.mode", "FAIR")
    val sc = new SparkContext(conf)
    for ((k, v) <- settings.hadoopConfigMap) sc.hadoopConfiguration.set(k, v)
    sscOpt = Some(new StreamingContext(sc, batchDuration))

    val inputStream = KafkaUtils.createStream[
      String, String, StringDecoder, AvroFlumeEventBodyDecoder](
      sscOpt.get, kafkaParams, topicMap, StorageLevel.MEMORY_AND_DISK_SER)
    processor.process(inputStream.map(_._2))

    sscOpt.get.start
    sscOpt.get.awaitTermination
  }

  def stop = {
    try {
      if (sscOpt.isDefined) sscOpt.get.stop(true, true)
    } catch {
      case e: Exception => LOG.error(getStackTraceStr(e))
    }
  }
}