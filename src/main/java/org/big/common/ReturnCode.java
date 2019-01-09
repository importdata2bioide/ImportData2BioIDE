package org.big.common;

/**
 * @Author: WangTianshan(王天山)
 * @Description:
 * @Created Date: 2017/11/10 15:33
 * @Modified By:
 * @Last Modified Date:
 */
public enum ReturnCode {
    SUCCESS("0","success","成功"),
    FAILURE("1","failure","失败"),
    SUCCESS_UPLOAD("2","upload success","上传成功"),
    RECORD_NULL("10000","record is null","记录为空"),
    SERVER_BUSY("-1","server is busy, please try later","服务器忙，请稍后再试"),
    INTERNAL_ERROR("-2","server internal error","服务器内部错误"),
    PARAM_ERROR("-3","param error","参数错误"),
    UNSUPPORTED_TYPE("-4","unsupported type","不支持的类型"),
    NOT_LOGGIN("41000","not login","未登录"),
    USER_FROZEN("41001","user was frozen","用户不可用"),
    NOT_AUTHORIZED("42000","not authorized","没有权限"),
    JSON_ERROR("47001","JSON/XML parse error","数据错误"),
    PERMISSION_DENIED("004","permission denied","没有权限");
    private String code; //code
    private String message;  //message
    private String message_zh;  //message_zh
    ReturnCode(String code, String message, String message_zh){
        this.code = code;
        this.message = message;
        this.message_zh = message_zh;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_zh() {
        return message_zh;
    }

    public void setMessage_zh(String message_zh) {
        this.message_zh = message_zh;
    }
}
