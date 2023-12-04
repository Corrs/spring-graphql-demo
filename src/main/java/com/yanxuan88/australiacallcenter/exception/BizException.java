package com.yanxuan88.australiacallcenter.exception;

/**
 * 业务异常
 *
 * @author co
 * @since 2023/12/1 上午9:49:50
 */
public class BizException extends RuntimeException {
    private final String code;
    private final String msg;

    public BizException(IResultCode iResultCode) {
        super(iResultCode.getCode() + iResultCode.getMessage());
        this.code = iResultCode.getCode();
        this.msg = iResultCode.getMessage();
    }

    public BizException(String code, String message) {
        super(code + message);
        this.code = code;
        this.msg = message;
    }

    public BizException(String message) {
        super(message);
        this.code = BaseResultCodeEnum.BIZ_ERROR.getCode();
        this.msg = message;
    }


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
