package com.alphasigma.kronos;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

public class ImprovedSlidingPaneLayout extends SlidingPaneLayout {
	
	Context context;
	FrameLayout left;
	FrameLayout right;
	Boolean canOpen = true;
	
	public ImprovedSlidingPaneLayout(Context context) {
	    super(context);
	    this.context = context;
	    this.left = new FrameLayout(context);
	    this.right = new FrameLayout(context);
	    this.addView(left);
	    this.addView(right);
	}
	public ImprovedSlidingPaneLayout(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    this.context = context;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
	    if (canOpen)
	        return super.onInterceptTouchEvent(ev);
	    else
	        return false;
	}

	public ImprovedSlidingPaneLayout canOpen(Boolean canOpen) {
	    this.canOpen = canOpen;
	    return this;
	}

	public ImprovedSlidingPaneLayout makeActionBarSlide(Window window){
	    ViewGroup decorView = (ViewGroup) window.getDecorView();
	    ViewGroup mainContent = (ViewGroup) decorView.getChildAt(0);
	    decorView.removeView(mainContent);
	    setContentView(mainContent);
	    decorView.addView(this);
	    return this;
	}

	public ImprovedSlidingPaneLayout setMenuView(View view){
	    if((left.getChildCount()== 1)){
	        left.removeView(left.getChildAt(0));
	    }
	    left.addView(view);
	    return this;
	}

	public ImprovedSlidingPaneLayout setContentView(View view){
	    if((right.getChildCount()== 1)){
	        right.removeView(right.getChildAt(0));
	    }
	    right.addView(view);
	    return this;
	}

	public ImprovedSlidingPaneLayout setMenuWidth(int width){
	    left.setLayoutParams(new SlidingPaneLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
	    return this;
	}
}
