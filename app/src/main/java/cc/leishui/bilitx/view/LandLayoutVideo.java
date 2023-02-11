package cc.leishui.bilitx.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import cc.leishui.bilitx.R;

/**
 * Created by shuyu on 2016/12/23.
 * CustomGSYVideoPlayer是试验中，建议使用的时候使用StandardGSYVideoPlayer
 */
public class LandLayoutVideo extends StandardGSYVideoPlayer {

    private boolean isLinkScroll = false;

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    public LandLayoutVideo(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public LandLayoutVideo(Context context) {
        super(context);
    }

    public LandLayoutVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Dialog mProgressDialog2;
    private boolean isLongPressed = false;

    //快进的dialog
    protected void showProgressDialog2() {
        if (mProgressDialog2 == null) {
            View localView = LayoutInflater.from(getActivityContext()).inflate(R.layout.video_progress_dialog2, null);
            mProgressDialog2 = new Dialog(getActivityContext(), R.style.video_style_dialog_progress);
            mProgressDialog2.setContentView(localView);
            mProgressDialog2.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog2.getWindow().addFlags(32);
            mProgressDialog2.getWindow().addFlags(16);
            mProgressDialog2.getWindow().setLayout(getWidth(), getHeight());
            WindowManager.LayoutParams localLayoutParams = mProgressDialog2.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.TOP;
            localLayoutParams.width = getWidth();
            localLayoutParams.height = getHeight();
            int[] location = new int[2];
            getLocationOnScreen(location);
            localLayoutParams.x = location[0];
            localLayoutParams.y = location[1];
            mProgressDialog2.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog2.isShowing()) {
            mProgressDialog2.show();
        }
    }

    protected void dismissProgressDialog2() {
        if (mProgressDialog2 != null) {
            mProgressDialog2.dismiss();
            mProgressDialog2 = null;
        }
    }

    @Override
    protected void touchSurfaceUp() {
        super.touchSurfaceUp();
        setSpeed(1f);
        dismissProgressDialog2();
        isLongPressed = false;
    }

    /**
     * ===================================处理toolbar显示隐藏=========================
     */

    private ShowOrHideListener listener;

    public interface ShowOrHideListener {
        void show();

        void hide();
    }

    public void setShowOrHideListener(ShowOrHideListener listener) {
        this.listener = listener;
    }

//    @Override
//    protected void changeUiToPreparingShow() {
//        if (listener != null)
//            listener.show();
//        super.changeUiToPreparingShow();
//    }
//
//    @Override
//    protected void hideAllWidget() {
//        if (listener != null)
//            listener.hide();
//        super.hideAllWidget();
//    }

    @Override
    protected void setViewShowState(View view, int visibility) {
        if (view == mBottomProgressBar && listener != null) {
            if (visibility == VISIBLE)
                listener.hide();
            else
                listener.show();
        }
        super.setViewShowState(view, visibility);
    }

    /**
     * ===================================屏蔽长按产生的冲突=========================
     */
    @Override
    protected void touchSurfaceMove(float deltaX, float deltaY, float y) {
        if (isLongPressed) return;
        super.touchSurfaceMove(deltaX, deltaY, y);
    }

    @Override
    protected void touchSurfaceDown(float x, float y) {
        if (isLongPressed) return;
        super.touchSurfaceDown(x, y);
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        if (isLongPressed) return;
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
    }

    /**
     * ==========================================================================
     */

    @SuppressLint("SetTextI18n")
    @Override
    protected void init(Context context) {
        super.init(context);

        post(new Runnable() {
            @Override
            public void run() {
                gestureDetector = new GestureDetector(getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        touchDoubleUp(e);
                        return super.onDoubleTap(e);
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness
                                && mCurrentState != CURRENT_STATE_ERROR
                        ) {
                            onClickUiToggle(e);
                        }
                        return super.onSingleTapConfirmed(e);
                    }

                    @Override
                    public void onLongPress(@NonNull MotionEvent e) {
                        if (mCurrentState==CURRENT_STATE_PLAYING) {
                            setSpeed(2f);
                            showProgressDialog2();
                            isLongPressed = true;
                        }
                        super.onLongPress(e);
                    }
                });
            }
        });
    }


    //这个必须配置最上面的构造才能生效
    @Override
    public int getLayoutId() {
        if (mIfCurrentIsFullscreen) {
            return R.layout.sample_video_land;
        }
        return R.layout.sample_video_normal;
    }

    @Override
    protected void updateStartImage() {
        if (mIfCurrentIsFullscreen) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(R.drawable.video_click_play_selector);
                }
            }
        } else {
            super.updateStartImage();
        }
    }

//    @Override
//    public int getEnlargeImageRes() {
//        return R.drawable.custom_enlarge;
//    }
//
//    @Override
//    public int getShrinkImageRes() {
//        return R.drawable.custom_shrink;
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isLinkScroll && !isIfCurrentIsFullscreen()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        LandLayoutVideo landLayoutVideo = (LandLayoutVideo) gsyVideoPlayer;
        landLayoutVideo.dismissProgressDialog();
        landLayoutVideo.dismissVolumeDialog();
        landLayoutVideo.dismissBrightnessDialog();
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
    }

    public void setLinkScroll(boolean linkScroll) {
        isLinkScroll = linkScroll;
    }


    /**
     * 定义结束后的显示
     */
    @Override
    protected void changeUiToCompleteClear() {
        super.changeUiToCompleteClear();
        setTextAndProgress(0, true);
        //changeUiToNormal();
    }

    @Override
    protected void changeUiToCompleteShow() {
        super.changeUiToCompleteShow();
        setTextAndProgress(0, true);
        //changeUiToNormal();
    }
}
