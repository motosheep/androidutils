package com.north.light.libshare.wechat;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * 微信分享interface
 */
public interface IWeChatShare {

    /**
     * 初始化
     */
    void wxShareInit(Context context, final String APP_ID);

    /**
     * 分享链接
     *
     * @param title 标题
     * @param desc  描述内容
     * @param type  类型：1微信好友，2朋友圈
     */
    void wxShareLink(String title, String desc, String link, int resId, int type);


    /**
     * 分享文字
     *
     * @param content 文字内容
     * @param desc    描述内容
     * @param type    类型：1微信好友，2朋友圈
     */
    void wxShareTxt(String content, String desc, int type);

    /**
     * 分享resource图片
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    void wxShareResPic(int resId, int width, int height, int type);

    /**
     * 分享路径图片
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    void wxSharePathPic(String path, int width, int height, int type);

    /**
     * 分享bitmap图片
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    void wxShareBitmapPic(Bitmap path, int width, int height, int type);

    /**
     * 分享音乐
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    void wxShareMusic(String url, String content, String desc, String cover, int width, int height, int type);


    /**
     * 分享视频
     *
     * @param type 类型：1微信好友，2朋友圈
     */
    void wxShareVideo(String url, String content, String desc, String cover, int width, int height, int type);

}
