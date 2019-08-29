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

    // Login Module

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
}
