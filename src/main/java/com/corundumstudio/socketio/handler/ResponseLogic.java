/**
 * Copyright 2012 Nikita Koksharov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.corundumstudio.socketio.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

public class ResponseLogic {
    public static void sendError(
        ChannelHandlerContext ctx, String code, String message, String origin
    ) {
        HttpResponse res = new DefaultFullHttpResponse(
            HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
            Unpooled.copiedBuffer(
                "{code: '" + code + "', message: '" + message + "'}", // GSONBuilder.generatObject
                CharsetUtil.UTF_8
            )
        );

        HttpHeaders hdrs = res.headers();
        hdrs.add("Content-Type", "application/json");

        if ( origin != null ) {
            hdrs.add("Access-Control-Allow-Credentials", "true");
            hdrs.add("Access-Control-Allow-Origin", origin);
        } else {
            hdrs.add("Access-Control-Allow-Origin", "*");
        }

        ctx.channel().writeAndFlush(res).addListener(ChannelFutureListener.CLOSE);
    }
}
