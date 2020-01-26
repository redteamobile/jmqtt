package org.jmqtt.broker.processor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.jmqtt.common.bean.Message;
import org.jmqtt.common.log.LoggerName;
import org.jmqtt.remoting.netty.RequestProcessor;
import org.jmqtt.remoting.util.MessageUtil;
import org.jmqtt.remoting.util.NettyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingProcessor implements RequestProcessor {

    private Logger log = LoggerFactory.getLogger(LoggerName.MESSAGE_TRACE);

    @Override
    public void processRequest(ChannelHandlerContext ctx, MqttMessage mqttMessage) {
        //log.info("[PingMessage] get ping package from client {}" , NettyUtil.getClientId(ctx.channel()));
        MqttMessage pingRespMessage = MessageUtil.getPingRespMessage();
        //log.info("[PingMessage] return ping resMessage {} to client {}" , pingRespMessage , NettyUtil.getClientId(ctx.channel()));

        ctx.writeAndFlush(pingRespMessage);
    }
}
