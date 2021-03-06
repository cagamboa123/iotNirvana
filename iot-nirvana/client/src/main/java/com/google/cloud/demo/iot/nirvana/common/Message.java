/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.cloud.demo.iot.nirvana.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Message class describes a message generated by a simulated device that will be published into
 * PubSub and then streamed into DataStore and BigQuery via DataFlow.
 */
public class Message {

  String id;
  double temperature;
  long timestamp;

  /** Default constructor. */
  public Message() {}

  /**
   * Create a new message
   *
   * @param id the id of the message MD5(lat+"-"+lng)
   * @param temperature the temperature to be published
   * @param timestamp the UNIX epoch time
   */
  public Message(String id, double temperature, long timestamp) {
    this.id = id;
    this.temperature = temperature;
    this.timestamp = timestamp;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setId(double latitude, double longitude) throws TemperatureException {
    try {
      String idStr = latitude + "-" + longitude;
      MessageDigest md = MessageDigest.getInstance("MD5");
      this.id = toHexString(md.digest(idStr.getBytes("UTF-8")));
    } catch (NoSuchAlgorithmException e) {
      throw new TemperatureException(
          String.format("Cannot set message id. Cause: {}", e.getMessage()), e);
    } catch (UnsupportedEncodingException e) {
      throw new TemperatureException(
          String.format("Cannot set message id. Cause: {}", e.getMessage()), e);
    }
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Convert a bytes array into a HEX string
   *
   * @param bytes the bytes array that you want to convert
   * @return an HEX string representing the bytes array
   */
  private String toHexString(byte[] bytes) {
    StringBuilder hexString = new StringBuilder();
    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(0xFF & bytes[i]);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }

    return hexString.toString().toUpperCase();
  }
}
