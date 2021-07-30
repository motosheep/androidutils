package com.north.light.libshare;

import android.content.Context;

import com.north.light.libshare.contentprovider.ShareContext;
import com.north.light.libshare.wechat.WeChatShare;

/**
 * 分享的主类
 */
public class ShareMain {
    private Context mContext;

    private static final class SingleHolder {
        static final ShareMain mInstance = new ShareMain();
    }

    public static ShareMain getInstance() {
        return SingleHolder.mInstance;
    }


    /**
     * 初始化---使用前必须调用
     */
    public void init(String wxAppId) {
        this.mContext = ShareContext.getInstance().getContext();
        WeChatShare.getInstance().wxShareInit(mContext, wxAppId);
    }

    /**
     * 获取微信分享对象
     */
    public WeChatShare getWeChatShare() {
        return WeChatShare.getInstance();
    }


}
