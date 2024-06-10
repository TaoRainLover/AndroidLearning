package org.zt.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;

/**
 * 自定义视图
 */

public class CustomView extends View implements LifecycleEventObserver {
    private float mWidth;
    private float mHeight;

    // 半径
    private float mRadius;

    // 转动角度
    private float mAngle = 10;

    // 实线画笔
    private Paint solidLinePaint = new Paint();
    private Paint dashLinePaint = new Paint();
    // 文本画笔
    private TextPaint textPaint = new TextPaint();
    // 向量画笔
    private Paint vectorLinePaint = new Paint();

    // 使用静态代码块初始化画笔
    {
        solidLinePaint.setStyle(Paint.Style.STROKE);
        solidLinePaint.setStrokeWidth(5f);
        solidLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

        dashLinePaint.setStyle(Paint.Style.STROKE);
        dashLinePaint.setPathEffect(new DashPathEffect(new float[] {10f, 10f}, 0f));
        dashLinePaint.setStrokeWidth(5f);
        dashLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue));

        textPaint.setTextSize(40f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

        vectorLinePaint.setStyle(Paint.Style.STROKE);
        vectorLinePaint.setStrokeWidth(5f);
        vectorLinePaint.setColor(ContextCompat.getColor(getContext(), R.color.blue));
    }

    private final static String TAG = "CustomView";
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate: ");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
    }
 
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout: ");
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: ");

        drawAxises(canvas);
        drawLabel(canvas);
        drawDashCircle(canvas);
        drawVector(canvas);
    }

    // 绘制坐标轴
    private void drawAxises(Canvas canvas) {
        // 绝对坐标绘制
        // canvas.drawLine(100f, 100f, 100f, 400f, solidLinePaint);

        // 将坐标原点设置到 View 中心进行绘制
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawLine(-mWidth / 2, 0f, mWidth / 2, 0, solidLinePaint);
        canvas.drawLine(0, -mHeight / 2, 0, mHeight / 2, solidLinePaint);
        canvas.restore();

        // 将坐标原点设置到 View x轴中心， y轴四分之三位置进行绘制
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 4 * 3);
        canvas.drawLine(-mWidth / 2, 0f, mWidth / 2, 0, solidLinePaint);
        canvas.restore();
    }

    // 绘制文本标签
    public void drawLabel(Canvas canvas) {
        canvas.drawRect(40f, 60f, 450f, 160f, solidLinePaint);
        canvas.drawText("指数函数与旋转矢量", 60f, 130f, textPaint);
    }

    // 绘制虚线圆圈
    public void drawDashCircle(Canvas canvas) {
        // 将坐标原点设置到 View x轴中心， y轴四分之三位置进行绘制
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 4 * 3);
        canvas.drawCircle(0f, 0f, mRadius, dashLinePaint);
        canvas.restore();
    }

    // 绘制旋转的向量直线
    public void drawVector(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 4 * 3);
        canvas.rotate(mAngle);
        canvas.drawLine(0f, 0f, mRadius, 0f, vectorLinePaint);
        canvas.restore();
    }

    private void startRotating() {
        CoroutineScope coroutineScope = new CoroutineScope() {
            @NonNull
            @Override
            public CoroutineContext getCoroutineContext() {
                return null;
            }
        };
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            // 更新角度值
            mAngle += 5f;

            // 重绘视图
            invalidate();

            // 再次执行Runnable，实现类似协程的延迟效果
            mainHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow: ");
    }

    // 当 View 的最终尺寸确定之后进行回调
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: ");
        mWidth = w;
        mHeight = h;
        mRadius = (float) (w < h / 2 ? w / 2.0 : h / 4.0) - 20f;
    }


    // 启动周期性更新
    public void startPeriodicUpdates() {
        mainHandler.post(updateRunnable);
    }

    // 停止周期性更新
    public void stopPeriodicUpdates() {
        mainHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {

    }
}
