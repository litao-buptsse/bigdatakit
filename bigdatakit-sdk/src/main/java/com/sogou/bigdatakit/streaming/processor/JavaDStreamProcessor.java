package com.sogou.bigdatakit.streaming.processor;

import org.apache.spark.streaming.api.java.JavaDStream;

import java.io.Serializable;

/**
 * Created by Tao Li on 6/16/16.
 */
public abstract class JavaDStreamProcessor implements Serializable {
  public abstract void process(JavaDStream<String> dstream);
}
