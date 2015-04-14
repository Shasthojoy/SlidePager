/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Omada Health, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.omadahealth.slidepager.lib.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.github.omadahealth.slidepager.lib.R;
import com.github.omadahealth.slidepager.lib.interfaces.OnSlidePageChangeListener;
import com.github.omadahealth.slidepager.lib.interfaces.OnWeekListener;
import com.github.omadahealth.typefaceview.TypefaceTextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by stoyan on 4/7/15.
 */
public class WeekSlideView extends LinearLayout{
    /**
     * The tag for logging
     */
    private static final String TAG = "WeekSlideView";

    /**
     * An array that holds all the {@link DayProgressView} for this layout
     */
    private List<DayProgressView> mDays = new ArrayList<>(7);

    /**
     * The left textview
     */
    private TypefaceTextView mLeftTextView;

    /**
     * The right textview
     */
    private TypefaceTextView mRightTextView;

    /**
     * True of we want to show {@link #mLeftTextView}
     */
    private boolean mShowLeftText;

    /**
     * True of we want to show {@link #mRightTextView}
     */
    private boolean mShowRightText;

    /**
     * The current day sliding {@link android.widget.ImageView} we display
     * below the currently selected {@link DayProgressView} from {@link #mDays}
     */
    private SelectedImageView mSelectedImageView;

    /**
     * The callback listener for when views are clicked
     */
    private OnWeekListener mCallback;

    /**
     * The default animation time
     */
    private static final int DEFAULT_PROGRESS_ANIMATION_TIME = 1000;

    private TypedArray mAttributes;

    /**
     * The {@link AnimatorSet} used to animate the Slider selected day
     */
    private AnimatorSet mAnimationSet;

    /**
     * The animation time in milliseconds that we animate the progress
     */
    private int mProgressAnimationTime = DEFAULT_PROGRESS_ANIMATION_TIME;


    public WeekSlideView(Context context) {
        this(context, null);
    }
    public WeekSlideView(Context context, TypedArray attributes) {
        super(context, null);
        init(context, attributes);
//        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                animateSelectedTranslation(mDays.get(0));
//            }
//        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        animateSelectedTranslation(mDays.get(0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

//        animateSelectedTranslation(mDays.get(0));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        animateSelectedTranslation(mDays.get(0));
    }

    private void loadStyledAttributes(TypedArray attributes) {
        mAttributes = attributes;
        if (mAttributes != null) {
            mShowLeftText = attributes.getBoolean(R.styleable.SlidePager_slide_show_week, true);
            mShowRightText = attributes.getBoolean(R.styleable.SlidePager_slide_show_date, true);

            mLeftTextView.setVisibility(mShowLeftText ? VISIBLE : GONE);
            mRightTextView.setVisibility(mShowRightText ? VISIBLE : GONE);
        }
    }

    /**
     * Initiate the view and start butterknife injection
     *
     * @param context
     */
    private void init(Context context,TypedArray attributes) {
        if (!isInEditMode()) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.view_week_slide, this);
            ButterKnife.inject(this, view);

            mAnimationSet = new AnimatorSet();
            injectViews();
            setListeners();
            loadStyledAttributes(attributes);
        }
    }

    /**
     * Inject the views into {@link #mDays}
     */
    private void injectViews() {
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_1).loadStyledAttributes(mAttributes));
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_2).loadStyledAttributes(mAttributes));
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_3).loadStyledAttributes(mAttributes));
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_4).loadStyledAttributes(mAttributes));
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_5).loadStyledAttributes(mAttributes));
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_6).loadStyledAttributes(mAttributes));
        mDays.add(ButterKnife.<DayProgressView>findById(this, R.id.day_progress_7).loadStyledAttributes(mAttributes));

        mLeftTextView = ButterKnife.findById(this, R.id.left_textview);
        mRightTextView = ButterKnife.findById(this, R.id.right_textview);

        mSelectedImageView = ButterKnife.findById(this, R.id.selected_day_image_view);
        mSelectedImageView.setSelectedViewId(mDays.get(3).getId());


    }

    /**
     * Animates the translation of the {@link #mSelectedImageView}
     *
     * @param view The view to use to set the animation position
     */
    public void animateSelectedTranslation(View view) {
        final Float offset = -1 * this.getWidth() + view.getWidth() / 2 + view.getX();
        mSelectedImageView.setTag(R.id.selected_day_image_view, offset);
        mSelectedImageView.setSelectedViewId(view.getId());

        if(mAnimationSet == null){
            mAnimationSet = new AnimatorSet();
        }

        if(mAnimationSet.isRunning()){
           return;
        }
        mAnimationSet.playSequentially(Glider.glide(Skill.QuadEaseInOut, 1000, ObjectAnimator.ofFloat(mSelectedImageView, "x", mSelectedImageView.getX(), offset)));
        mAnimationSet.setDuration(1000);
        mAnimationSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimationSet.start();
    }

    /**
     * Set up listeners for all the views in {@link #mDays}
     */
    private void setListeners() {
        for (final DayProgressView dayProgressView : mDays) {
            if (dayProgressView != null) {
                dayProgressView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = dayProgressView.getIntTag();
                        animateSelectedTranslation(view);
                        if (mCallback != null) {
                            mCallback.onDaySelected(index);
                        }
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void animatePage(OnSlidePageChangeListener listener, TypedArray attributes) {
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    ((DayProgressView) child).loadStyledAttributes(attributes);
                    animateProgress((DayProgressView) child, children, listener);
                    animateSelectedTranslation(mDays.get(0));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void animateSeries(boolean show) {
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    final DayProgressView dayProgressView = (DayProgressView) child;
                    dayProgressView.showStreak(show, DayProgressView.STREAK.RIGHT_STREAK);
                    dayProgressView.showStreak(show, DayProgressView.STREAK.LEFT_STREAK);
                    dayProgressView.showCheckMark(show);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void resetPage(TypedArray mAttributes) {
        loadStyledAttributes(mAttributes);
        animateSeries(false);
        getSelectedImageView().resetView();
        final List<View> children = (List<View>) getTag();
        if (children != null) {
            for (final View child : children) {
                if (child instanceof DayProgressView) {
                    DayProgressView dayProgressView = (DayProgressView) child;
                    dayProgressView.reset();
                }
            }
        }
    }

    private void animateProgress(DayProgressView view, List<View> children, OnSlidePageChangeListener listener) {
        if (listener != null) {
            int progress = listener.getDayProgress(view.getIntTag());
            view.animateProgress(0, progress, mProgressAnimationTime, children);
        }
    }

    /**
     * Sets the listener for click events in this view
     *
     * @param listener
     */
    public void setListener(OnWeekListener listener) {
        this.mCallback = listener;
    }

    public SelectedImageView getSelectedImageView() {
        return mSelectedImageView;
    }
}