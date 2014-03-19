package ru.yandex.selo.czarskoe.Molitvoslov;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ActivityText extends Activity {

    TextView myTextView;
    Button btnNext;
    Button btnPrev;
    Button btnCenter;
    LinearLayout layoutText;
    LinearLayout layoutFooter;

    int mScrollTo;
    int mScrollToY;
    int mPagesCount;
    int mCurrentPageNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mScrollToY = -1;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.text_page);

        if(mCurrentPageNumber > 0)
            return;

        mScrollTo = 0;
        mCurrentPageNumber = 1;

        InitGui();
        InitTextView();
        SetListeners();
        AfterOnCreate();
    }

    private void AfterOnCreate(){
        ViewTreeObserver vto = myTextView.getViewTreeObserver();
        assert vto != null;
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if(mCurrentPageNumber > 1)
                {
                    myTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mScrollToY != -1)
                                myTextView.scrollTo(0, mScrollToY);

                            CutOffTextLayout();
                            ShowPageNumber();
                        }
                    });
                }
                else
                {
                    CutOffTextLayout();
                    ShowPageNumber();

                    int ctr = 0;
                    int y = 0;
                    int y_old = -1;
                    while(y >= 0 && y !=y_old)
                    {
                        y_old = y;
                        y = ScrollDown();
                        ctr++;
                    }
                    mPagesCount = ctr;
                    mPagesCount-=1;
                    ScrollTo0();
                }
            }
        });
    }

    private void InitGui(){
        btnPrev = (Button) findViewById(R.id.buttonPrev);
        btnNext = (Button) findViewById(R.id.buttonNext);
        btnCenter = (Button) findViewById(R.id.buttonCenter);

        layoutText = (LinearLayout) findViewById(R.id.listviewlayout);
        layoutFooter = (LinearLayout) findViewById(R.id.footerlayout);

        myTextView = (TextView) findViewById(R.id.textView1);

        Bundle b = getIntent().getExtras();
        assert b != null;
        int value = b.getInt("key");
        if(value == 0 || value == 1 )
        {
            Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/HirmosPonomar.ttf");
            myTextView.setTypeface(myTypeface);
        }
        else
        {
            //Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/HirmosPonomar.ttf");
            //myTextView.setTypeface("Sans");
        }
        myTextView.setTextColor(0xff000000);
        myTextView.setBackgroundColor(0xffFFF0D1);

        btnPrev.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HirmosPonomar.ttf"));
        btnNext.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/HirmosPonomar.ttf"));
    }

    private void InitTextView(){
        Bundle b = getIntent().getExtras();
        assert b != null;
        int value = b.getInt("key");
        String line = "";
        if(value == 0 || value == 1 || value == 2 || value == 3){
            try{
                int id = R.raw.text;
                if(value==0)id =              R.raw.text;
                if(value==1)id =              R.raw.text2;
                if(value==2)id =              R.raw.text3;
                if(value==3)id =              R.raw.text4;
                BufferedReader in = new BufferedReader(new InputStreamReader(getResources().openRawResource(id)));
                String sCurrentLine;
                while ((sCurrentLine = in.readLine()) != null) {
                    if(sCurrentLine.isEmpty())
                        line += "\r\n\r\n";
                    else
                        line += sCurrentLine;
                }
                in.close();
            }
            catch (Exception e) {}
        }
        myTextView.setText(Html.fromHtml(line));
    }

    private void SetListeners(){
        View.OnClickListener oclBtnPrev = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prev();
            }
        };
        btnPrev.setOnClickListener(oclBtnPrev);

        View.OnClickListener oclBtnNext = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next();
            }
        };
        btnNext.setOnClickListener(oclBtnNext);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    Prev();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    Next();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void Next(){
        mCurrentPageNumber += 1;
        if(mCurrentPageNumber > mPagesCount){
            mCurrentPageNumber = mPagesCount;
            return;
        }
        mScrollToY = ScrollDown();

        CutOffTextLayout();
        ShowPageNumber();
    }

    private void Prev(){
        mCurrentPageNumber -= 1;
        if(mCurrentPageNumber<=1)
            mCurrentPageNumber = 1;

        mScrollToY = ScrollUp();
        CutOffTextLayout();
        ShowPageNumber();
    }

    private void CutOffTextLayout(){
        Rect bounds = new Rect();
        tvLayout().getLineBounds(lastVisibleLine(), bounds);
        layoutText.setBottom(bounds.top - scrollY()+10);
    }
    private void ShowPageNumber(){
        btnCenter.setText(Integer.toString(mCurrentPageNumber) + "/" + Integer.toString(mPagesCount));
    }
    private void ScrollTo0(){

        mScrollTo = 0;
        myTextView.scrollTo(0, 0);
    }
    private int ScrollDown(){
        mScrollTo +=  visibleLinesCount();
        int y = myTextView.getLayout().getLineTop(mScrollTo);
        myTextView.scrollTo(0, y);
        return y;
    }
    private int ScrollUp(){
        mScrollTo -=  visibleLinesCount();
        if(mScrollTo < 0)
            mScrollTo = 0;
        int y = myTextView.getLayout().getLineTop(mScrollTo);
        myTextView.scrollTo(0, y);
        return y;
    }

    private int scrollY(){
        return myTextView.getScrollY();
    }
    private int tvHeight(){
        int height = myTextView.getHeight();
        if(height > 0)
            return myTextView.getHeight();
        return myTextView.getMeasuredHeight();
    }
    private Layout tvLayout(){
        return myTextView.getLayout();
    }
    private int firstVisibleLine(){
        return tvLayout().getLineForVertical(scrollY());
    }
    private int lastVisibleLine(){
        int y2 = tvHeight();
        int y1 = scrollY();
        return tvLayout().getLineForVertical(scrollY()+tvHeight());
    }
    private int visibleLinesCount(){
        return lastVisibleLine() - firstVisibleLine();
    }

}