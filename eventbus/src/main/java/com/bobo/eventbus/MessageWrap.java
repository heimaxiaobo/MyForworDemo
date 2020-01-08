package com.bobo.eventbus;

/**
 * EventBus消息包装类
 * @author 陈锦波  2019/1/4
 */
public class MessageWrap {

    public final String message;

    public static MessageWrap getInstance(String message) {
        return new MessageWrap(message);
    }

    private MessageWrap(String message) {
        this.message = message;
    }
}