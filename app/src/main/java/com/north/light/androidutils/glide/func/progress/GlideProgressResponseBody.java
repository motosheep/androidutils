package com.north.light.androidutils.glide.func.progress;

import android.util.Log;

import androidx.annotation.Nullable;


import com.north.light.androidutils.glide.func.progress.callback.GlideProgressManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class GlideProgressResponseBody extends ResponseBody {

    private static final String TAG = "ProgressResponseBody";

    private BufferedSource mBufferedSource;
    private ResponseBody mResponseBody;
    //    private GlideProgressListener mGlideProgressListener;
    private String mUrl;

    public GlideProgressResponseBody(String url, ResponseBody responseBody) {
        mUrl = url;
        mResponseBody = responseBody;
//        mGlideProgressListener = GlideProgressInterceptor.getListenerMap().get(url);
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {

        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(new ProgressSource(mResponseBody.source()));
        }

        return mBufferedSource;
    }

    private class ProgressSource extends ForwardingSource {

        private long mTotalBytesRead;
        private int mCurrentProgress;

        public ProgressSource(Source delegate) {
            super(delegate);
        }

        @Override
        public long read(Buffer sink, long byteCount) throws IOException {

            long bytesRead = super.read(sink, byteCount);
            long fullLength = mResponseBody.contentLength();

            if (bytesRead == -1) {
                mTotalBytesRead = fullLength;
            } else {
                mTotalBytesRead += bytesRead;
            }

            int progress = (int) (100f * mTotalBytesRead / fullLength);

            Log.e(TAG, "download url is： " + mUrl + " progress is " + progress);

            GlideProgressManager.getInstance().notifyProgress(mUrl, progress, (mTotalBytesRead == fullLength));

//            if ((mGlideProgressListener != null) && (progress != mCurrentProgress)) {
//                mGlideProgressListener.onProgress(progress);
//
//            }
//
//            if ((mGlideProgressListener != null) && (mTotalBytesRead == fullLength)) {
//                mGlideProgressListener = null;
//            }

            mCurrentProgress = progress;

            return bytesRead;
        }
    }
}
