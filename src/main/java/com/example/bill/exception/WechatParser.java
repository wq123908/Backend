package com.example.bill.exception;

/**
 * @Author admin
 * @Date 2025/7/7 18:04
 * @version 1.0
 */public class WechatParser extends RuntimeException {
  public WechatParser(String message) {
    super(message);
  }
}
