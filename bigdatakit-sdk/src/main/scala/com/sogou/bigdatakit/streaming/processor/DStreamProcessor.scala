package com.sogou.bigdatakit.streaming.processor

import org.apache.spark.streaming.dstream.DStream

/**
  * Created by Tao Li on 6/16/16.
  */
trait DStreamProcessor extends java.io.Serializable {
  def process(dstream: DStream[String])
}
