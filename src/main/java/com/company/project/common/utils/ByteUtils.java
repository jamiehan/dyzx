package com.company.project.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.company.project.common.constant.Common;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

public class ByteUtils {

    public static byte[] byteMergerAll(byte[]... values) {
        int length_byte = 0;
        for (int i = 0; i < values.length; i++) {
            length_byte += values[i].length;
        }
        byte[] all_byte = new byte[length_byte];
        int countLength = 0;
        for (int i = 0; i < values.length; i++) {
            byte[] b = values[i];
            System.arraycopy(b, 0, all_byte, countLength, b.length);
            countLength += b.length;
        }
        return all_byte;
    }

    /**
     * 将byte数组转换为整数
     */
    public static int bytesToInt(byte[] bs) {
        int a = 0;
        for (int i = bs.length - 1; i >= 0; i--) {
            a += bs[i] * Math.pow(0xFF, bs.length - i - 1);
        }
        return a;
    }

    /* byte[]->int */
    public final static int getInt(byte[] buf, boolean asc) {
        if (buf == null) {
            throw new IllegalArgumentException("byte array is null!");
        }
        if (buf.length > 4) {
            throw new IllegalArgumentException("byte array size > 4 !");
        }
        int r = 0;
        if (asc)
            for (int i = buf.length - 1; i >= 0; i--) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        else
            for (int i = 0; i < buf.length; i++) {
                r <<= 8;
                r |= (buf[i] & 0x000000ff);
            }
        return r;
    }

    /**
     * 编辑发送数据
     * @param t
     * @param <T>
     * @return
     */
    public static <T> ByteBuf editSendData(String cmdType, T t) {
        // 帧头
        byte[] header= new byte[]{0x5B,0x5B};
        // 方向（0x00:算法 -> 后台发送数据；0x01:后台 -> 算法发送数据）
        byte[] direct = new byte[]{0x01};
        // 类型
        byte[] type = new byte[]{};
        switch (cmdType){
            case Common.CommandType.CMD:
                // 类型 (0x90:命令帧)
                type = new byte[]{(byte)0x90};
                break;
            case Common.CommandType.DATA:
                // 类型 (0x91:数据帧)
                type = new byte[]{(byte)0x91};
                break;
            case Common.CommandType.ACK:
                // 类型 (0x92:响应帧)
                type = new byte[]{(byte)0x92};
                break;
            default:
        }

        // CRC校验位
        byte[] crc = new byte[]{0x00,0x00};
        // 帧尾
        byte[] tail= new byte[]{(byte) 0xB5,(byte) 0xB5};

        byte[] content = new byte[]{};
        byte[] size = new byte[]{};
        if ( t != null) {
            // 获取
            String contStr = JSONArray.toJSON(t).toString();
            content = contStr.getBytes();
        }

        int length = content.length;
        size = ByteBuffer.allocate(4).putInt(length).array();

        byte[] mergedData = ByteUtils.byteMergerAll(header, direct, type, size, content, crc, tail);

        return Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(mergedData));
    }

}
