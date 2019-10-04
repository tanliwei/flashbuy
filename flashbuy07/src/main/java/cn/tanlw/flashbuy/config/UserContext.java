package cn.tanlw.flashbuy.config;

import cn.tanlw.flashbuy.domain.FlashbuyUser;

public class UserContext {
    private static ThreadLocal<FlashbuyUser> userHolder = new ThreadLocal<>();
    public static void setUser(FlashbuyUser flashbuyUser){
        userHolder.set(flashbuyUser);
    }
    public FlashbuyUser getUser(){
        return userHolder.get();
    }
}
