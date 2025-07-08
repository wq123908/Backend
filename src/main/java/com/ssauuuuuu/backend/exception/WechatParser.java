package com.ssauuuuuu.backend.exception;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

/**
 * @version 1.0
 * @Author admin
 * @Date 2025/7/7 18:04
 */
public class WechatParser extends RuntimeException {
    public WechatParser() {
        super(String.valueOf(message));
    }
}
