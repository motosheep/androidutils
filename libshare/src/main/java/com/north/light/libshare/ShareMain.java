package com.north.light.libshare;

import android.content.Context;

import com.north.light.libshare.contentprovider.ShareContext;
import com.north.light.libshare.qq.QQShareImpl;
import com.north.light.libshare.wechat.WeChatShareImpl;

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
    public void init(String wxAppId, String qqAppId) {
        this.mContext = ShareContext.getInstance().getContext();
        WeChatShareImpl.getInstance().wxShareInit(mContext, wxAppId);
        QQShareImpl.getInstance().init(qqAppId);
    }

    /**
     * 获取微信分享对象
     */
    public WeChatShareImpl getWeChatShare() {
        return WeChatShareImpl.getInstance();
    }

    /**
     * 获取QQ分享对象
     */
    public QQShareImpl getQQShare() {
        return QQShareImpl.getInstance();
    }

}
