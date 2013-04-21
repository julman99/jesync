package com.github.julman99.jesync.net;

import com.github.julman99.jesync.core.LockEngine;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class ServerPipelineFactory implements ChannelPipelineFactory {
    private LockEngine engine;
    
    public ServerPipelineFactory(LockEngine engine){
        this.engine=engine;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = new DefaultChannelPipeline();

        // Add the text line codec combination first,
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        // and then business logic.
        pipeline.addLast("handler", new ServerHandler(this.engine));

        return pipeline;
    }
}