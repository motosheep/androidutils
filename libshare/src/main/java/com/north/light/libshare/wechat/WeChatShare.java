package com.north.light.libshare.wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

/**
 * 微信分享工具类
 */
public class WeChatShare implements IWeChatShare {
    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    private Context mContext;

    private static final class SingleHolder {
        static final WeChatShare mInstance = new WeChatShare();
    }

    public static WeChatShare getInstance() {
        return SingleHolder.mInstance;
    }

    /**
     * 注册
     */
    @Override
    public void wxShareInit(Context context, final String APP_ID) {
        this.mContext = context.getApplicationContext();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(APP_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    /**
     * 分享链接
     */
    @Override
    public void wxShareLink(String title, String desc, String link, int resId, int type) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(link)) {
            //内容为空，不处理
            return;
        }
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = link;

        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = desc;
        Bitmap thumbBmp = BitmapFactory.decodeResource(mContext.getResources(), resId);
        msg.thumbData = WxUtil.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
//        req.userOpenId = getOpenId();
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * 文字分享
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    @Override
    public void wxShareTxt(String content, String desc, int type) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(desc)) {
            //内容为空，不处理
            return;
        }
        if (type != 1 && type != 2) {
            return;
        }
        WXTextObject textObj = new WXTextObject();
        textObj.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // msg.title = "Will be ignored";
        msg.description = desc;
        msg.mediaTagName = "我是mediaTagName啊";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    /**
     * 分享resource图片
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    @Override
    public void wxShareResPic(int resId, int width, int height, int type) {
        if (type != 1 && type != 2) {
            return;
        }
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), resId);
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        bmp.recycle();
        msg.thumbData = WxUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    /**
     * 分享路径图片
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    @Override
    public void wxSharePathPic(String path, int width, int height, int type) {
        if (type != 1 && type != 2) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        bmp.recycle();
        msg.thumbData = WxUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    /**
     * 分享bitmap图片
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    @Override
    public void wxShareBitmapPic(Bitmap bitmap, int width, int height, int type) {
        if (type != 1 && type != 2) {
            return;
        }
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 40, 80, true);
//        bmp.recycle();
        msg.thumbData = WxUtil.bmpToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
//        req.scene = mTargetScene;
//        req.userOpenId = getOpenId();
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * 分享音乐
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    @Override
    public void wxShareMusic(String url, String content, String desc, String cover,
                             int width, int height, int type) {
        if (type != 1 && type != 2) {
            return;
        }
        WXMusicObject music = new WXMusicObject();
        //music.musicUrl = "http://www.baidu.com";
        music.musicUrl = url;
        //music.musicUrl="http://120.196.211.49/XlFNM14sois/AKVPrOJ9CBnIN556OrWEuGhZvlDF02p5zIXwrZqLUTti4o6MOJ4g7C6FPXmtlh6vPtgbKQ==/31353278.mp3";
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = content;
        msg.description = desc;
        File file = new File(cover);
        if (!file.exists()) {
            return;
        }
        Bitmap bmp = BitmapFactory.decodeFile(cover);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        bmp.recycle();
        msg.thumbData = WxUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }

    /**
     * 分享视频
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    @Override
    public void wxShareVideo(String url, String content, String desc, String cover,
                             int width, int height, int type) {
        if (type != 1 && type != 2) {
            return;
        }
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = url;
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = content;
        msg.description = desc;
        Bitmap bmp = BitmapFactory.decodeFile(cover);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        bmp.recycle();
        msg.thumbData = WxUtil.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        if (type == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (type == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }
        api.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


}
