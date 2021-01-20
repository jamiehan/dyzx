package com.company.project.common.exception.code;

/**
 * ResponseCodeInterface
 *
 * @author Jamie
 * @version V1.0
 * @date 2020年11月25日
 */
public interface ResponseCodeInterface {
    /**
     * 获取code
     * @return code
     */
    int getCode();

    /**
     * 获取信息
     * @return msg
     */
    String getMsg();
}
