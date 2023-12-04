package com.yanxuan88.australiacallcenter.exception;

/**
 * @author co
 * @since 2023/12/1 上午9:51:55
 */
public interface IResultCode {
    /**
     * 返回的错误码.
     *
     * @return String
     */
    String getCode();

    /**
     * 返回的错误信息.
     *
     * @return String
     */
    String getMessage();
}
