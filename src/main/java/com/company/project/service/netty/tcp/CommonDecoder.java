package com.company.project.service.netty.tcp;

import com.company.project.common.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/***
 * 通用解码器
 * @author Jamie
 * @date: 2021/1/11 16:01
 * @version : V1.0
 */
public class CommonDecoder extends ByteToMessageDecoder {

    // CHECKSTYLE:OFF
    private int faceRecogPort;
    private int otherPort;

    // 帧头
    byte[] header= new byte[]{0x5A,0x5A};

    public CommonDecoder(int faceRecogPort, int otherPort) {
        this.faceRecogPort = faceRecogPort;
        this.otherPort = otherPort;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {

        //拿到接收端口,分别解码
        String servicePort = ctx.channel().localAddress().toString();
        int port = Integer.parseInt(servicePort.split(":")[1]);

        String outMsg = null;
        String data = ByteBufUtil.hexDump(byteBuf);
        if(port == faceRecogPort){
            //调用人脸识别协议的拆包逻辑
            //长度小于12（包头8字节，校验位2字节，包尾2字节）个字节 不处理
            if(byteBuf.readableBytes() < 12){
                return ;
            }
            //不是固定的包头，则直接丢弃掉该数据，防止污染到后边的数据包长计算
            if(!data.startsWith(header.toString())){
                byteBuf.readBytes(byteBuf.readableBytes());
                return ;
            }

            //第5~8字节为数据长度
//            int len = ConvertUtil.convert16To10(DataParseUtil.startSmallAndEndBig(data.substring(4, 8)));
            //获取数据长度
            byte[] dataLenByte = new byte[4];
            byteBuf.getBytes(4, dataLenByte, 0, 4);
            int dataLen = ByteUtils.bytesToInt(dataLenByte);
            // 如果长度不够该包长，则发生了拆包
            if(data.length() < dataLen + 12){
                return ;
            }
            //如果长度超过该包长，则发生了粘包 当普通处理 只取固定长度
            outMsg = data.substring(0,dataLen + 12);
            byteBuf.readBytes(dataLen + 12);

        }else if(port == otherPort){
            //TODO 其他端口解码
        }
        if(outMsg != null){
            out.add(outMsg);
        }
        // CHECKSTYLE:ON
    }

}
