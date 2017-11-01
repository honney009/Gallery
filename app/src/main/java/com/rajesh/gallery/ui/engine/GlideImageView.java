package com.rajesh.gallery.ui.engine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * 提供加载图片宽高
 *
 * @author zhufeng on 2017/10/26
 */
@SuppressLint("AppCompatCustomView")
public class GlideImageView extends ImageView implements IRender {
    private static final String TAG = "GlideImageView";
    private Context mContext;
    private int mWidth = -1;
    private int mHeight = -1;

    private RequestListener<Uri, GlideDrawable> mUriRequestListener = new RequestListener<Uri, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            if (resource == null) {
                return false;
            }
            int preWidth = resource.getIntrinsicWidth();
            int preHeight = resource.getIntrinsicHeight();
            Log.i(TAG, "Glide drawable size:(" + preWidth + "," + preHeight + ")");
            if (preWidth != mWidth || preHeight != mHeight) {
                mWidth = preWidth;
                mHeight = preHeight;
                onRender(mWidth, mHeight);
            }
            return false;
        }
    };

    public GlideImageView(Context context) {
        this(context, null);
    }

    public GlideImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlideImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void loadImage(int resizeX, int resizeY, Uri uri) {
        Glide.with(mContext)
                .load(uri)
                .override(resizeX, resizeY)
                .priority(Priority.HIGH)
                .listener(mUriRequestListener)
                .into(this);
    }

    /**
     * load 1080P image
     *
     * @param uri
     */
    @Override
    public void loadImage(Uri uri) {
        loadImage(1080, 1920, uri);
    }

    @Override
    public void onRender(int width, int height) {

    }
}
