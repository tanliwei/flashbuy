package cn.tanlw.flashbuy.result;

/**
 * @Creator Tan Liwei
 * @Date 2019/8/29 22:08
 */
public class CodeMsg {
    private int code;
    private String msg;

    //General Exception
    public static CodeMsg SUCCESS = new CodeMsg(0, "Success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "Internal Server Error");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");

    // Login Module
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");

    // Goods Module

    // Order Module

    // Flash Buy Module

    private CodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
    
    public CodeMsg fillArgs(Object... args){
        String message = String.format(this.msg, args);
        return new CodeMsg(this.code, message);
    }
    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
