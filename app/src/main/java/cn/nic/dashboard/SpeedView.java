package cn.nic.dashboard;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nic on 2017/11/2.
 */

public class SpeedView extends View {
    private int startAngle;
    private int sweepAngle;
    private int startValue;
    private int rangeValue;
    private int sectionCount;
    private int portionCount;

    private int borderWidth=6;
    private int longIndex=18;

    private int w,h,r;
    private Paint mPaint;
    private Handler handHandler;
    private int value=0;

    public SpeedView(Context context) {
        super(context);
    }

    public SpeedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.SpeedView);
        startAngle=typedArray.getInt(R.styleable.SpeedView_startAngle,45);
        sweepAngle=typedArray.getInt(R.styleable.SpeedView_sweepAngle,267);
        startValue=(int)typedArray.getInt(R.styleable.SpeedView_startValue,0);
        rangeValue=(int)typedArray.getInt(R.styleable.SpeedView_rangeValue,260);
        sectionCount=(int)typedArray.getInt(R.styleable.SpeedView_sectionCount,26);
        portionCount=(int)typedArray.getInt(R.styleable.SpeedView_portionCount,5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        w = getWidth();
        h = getHeight();
        r = (int) (Math.min(w, h) / 2-borderWidth);
        initPaint();
        drawClock(canvas);
        drawShadeAtValue(canvas,value);
        drawHandAtValue(canvas,value);
    }
    //draw hand
    private  void drawHandAtValue(Canvas canvas,int atValue){
        int startR=(int) (0.9*r);
        int midR=(int)(startR-0.30*r);
        int endR=(int)(startR-0.36*r);
//         Log.d("nicDemo",r+"/"+startR+"/"+endR);
        int angleA=startAngle+sweepAngle*(atValue-startValue)/rangeValue;
        double radianA=Math.PI*angleA/180;
        float handStartX=(float) (w/2-startR*Math.sin(radianA));
        float handStartY=(float) (h/2+startR*Math.cos(radianA));
        float handEndX=(float)(w/2-endR*Math.sin(radianA));
        float handEndY=(float)(h/2+endR*Math.cos(radianA));
        float handMidX=(float)(w/2-midR*Math.sin(radianA));
        float handMidY=(float)(h/2+midR*Math.cos(radianA));
        float handMlX=(float)(w/2-midR*Math.sin(radianA+0.04));
        float handMlY=(float)(h/2+midR*Math.cos(radianA+0.04));
        float handMrX=(float)(w/2-midR*Math.sin(radianA-0.04));
        float handMrY=(float)(h/2+midR*Math.cos(radianA-0.04));

        Path path=new Path();
        path.moveTo(handStartX,handStartY);
        path.lineTo(handMlX,handMlY);
        path.lineTo(handEndX,handEndY);
        path.lineTo(handMrX,handMrY);
        path.close();

        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        canvas.drawPath(path,mPaint);
//        canvas.drawLine(handStartX,handStartY,handEndX,handEndY,mPaint);
    }

    private void drawShadeAtValue(Canvas canvas,int atValue){

        SweepGradient sweepGradient=new SweepGradient(
            w/2,h/2,
                new int[]{Color.GREEN,Color.YELLOW,Color.RED},
                null
        );
        mPaint.setShader(sweepGradient);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(26);
        canvas.save();
        canvas.rotate(90+startAngle,w/2,h/2);
        canvas.drawArc(
                new RectF(20*borderWidth,20*borderWidth,w-20*borderWidth,h-20*borderWidth),
                0,sweepAngle*(atValue-startValue)/rangeValue,
                false,
                mPaint
        );
        mPaint.setShader(null);
        canvas.restore();
    }

    private void drawClock(Canvas canvas){
//        draw panel, TODO extent
        drawPanel(canvas);
        //draw longIndex/shortIndex of the speed clock TODO extent
        drawIndex(canvas);
    }
    //TODO draw clock Panel
    private void drawPanel(Canvas canvas){
        mPaint.setColor(Color.CYAN);
//            canvas.drawCircle(w/2,h/2,r/3,mPaint);
        RectF rectF = new RectF(w/2-r,h/2-r,w/2+r,h/2+r);
        canvas.drawArc(rectF, startAngle + 90, sweepAngle, false, mPaint);

    }
    //TODO draw Index
    private void drawIndex(Canvas canvas){
        mPaint.setColor(Color.CYAN);

        int handsCount = sectionCount * portionCount + 1;
        canvas.save();
        canvas.rotate(startAngle - 180, w / 2, h / 2);
        for (int i = 0; i <= handsCount-1; i++) {
            if (i %portionCount == 0) {
                mPaint.setStrokeWidth(3);
                canvas.drawLine(w / 2, h/2-r, w / 2, longIndex+h/2-r, mPaint);
                canvas.rotate(sweepAngle / (handsCount-1), w / 2, h / 2);
            } else {
//                    mPaint.setStrokeWidth(2);
//                    canvas.drawLine(w / 2, h/2-r, w / 2, shortIndex+h/2-r, mPaint);
                canvas.rotate(sweepAngle /( handsCount-1), w / 2, h / 2);
            }
        }
        canvas.restore();
    }
    //INDIRECT METHOD
    private void initPaint(){
        mPaint=new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
    }

    public void setValue(int inputValue) {
        this.value = inputValue;
        postInvalidate();
    }
    public int getValue(){
        return value;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasured, heightMeasured);
        float ratio=2.43f;
        int default_width=222;
        int default_height=222;
        int w = measureSize(widthMeasureSpec,default_width);
        int h = measureSize(heightMeasureSpec,default_height);

        setMeasuredDimension(w,h);
    }

        //measure size
        private int measureSize(int measureSpec,int defaultSize){
            int mode = MeasureSpec.getMode(measureSpec);
            int size = MeasureSpec.getSize(measureSpec);
            int mSize = defaultSize;
            switch (mode){
                case MeasureSpec.EXACTLY:
                    mSize = size;
                    break;
                case MeasureSpec.AT_MOST:
                    mSize = defaultSize;
                    break;
                case MeasureSpec.UNSPECIFIED:
                    mSize = defaultSize;
                    break;
            }
            return mSize;
        }

}
