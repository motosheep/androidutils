package com.north.light.libshare.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.north.light.libshare.contentprovider.ShareContext;
import com.tencent.connect.share.QQShare;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * @Author: lzt
 * @CreateDate: 2021/8/4 15:07
 * @Version: 1.0
 * @Description: qq share impl
 */
public class QQShareImpl implements IQQShare {

    /**
     * tencent对象
     */
    private Tencent mTencent;

    private static final class SingleHolder {
        static final QQShareImpl mInstance = new QQShareImpl();
    }

    public static QQShareImpl getInstance() {
        return QQShareImpl.SingleHolder.mInstance;
    }

    /**
     * 初始化
     *
     * @param appId
     */
    @Override
    public void init(String appId) {
        mTencent = Tencent.createInstance(appId, ShareContext.getInstance().getContext());
    }


    /**
     * （3） 特别注意：
     * 应用调用Andriod_SDK接口时，如果要成功接收到回调，
     * 需要在调用接口的Activity的onActivityResult方法中增加如下代码：
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, new QQBaseUIListener() {
            @Override
            public void onComplete(Object o) {
                super.onComplete(o);
            }

            @Override
            public void onError(UiError uiError) {
                super.onError(uiError);
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

            @Override
            public void onWarning(int i) {
                super.onWarning(i);
            }
        });
    }

    /**
     * 分享图文信息
     */
    @Override
    public void shareImageTxt(Activity baseAct, String jumpUrl, String title, String desc, String picUrl,
                              String backTxt, String appName, int extra, QQBaseUIListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, desc);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, jumpUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, picUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(baseAct, params, listener);
    }


    /**
     * 分享纯图片
     * 分享图片大小不能超过5m
     */
    @Override
    public void shareImage(Activity baseAct, String appName, String picLocalPath, QQBaseUIListener listener) {
        Bundle params = new Bundle();
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, picLocalPath);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, appName);
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        mTencent.shareToQQ(baseAct, params, listener);
    }
}
