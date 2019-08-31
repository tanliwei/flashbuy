package cn.tanlw.flashbuy.result;

/**
 * @Creator Tan Liwei
 * @Date 2019/8/29 22:11
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result(CodeMsg cm) {
        if (cm == null) {
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    /**
     * Called in case of Success
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     * Called in case of Failure
     * @param cm
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    private Result(T data) {
        this.code = 0;
        this.msg = "Success";
        this.data = data;
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
