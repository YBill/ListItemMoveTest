package com.example.bill.listitemmovetest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private CustomLinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FrameLayout frameLayout;
    private int length;
    private int screenHeight;
    private int moveLength;
    private MyImageView moveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;

        length = (int) getResources().getDimension(R.dimen.dp_25);

        frameLayout = (FrameLayout) findViewById(R.id.frame);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        myAdapter = new MyAdapter(this);
        recyclerView.setLayoutManager(linearLayoutManager = new CustomLinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myAdapter);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        MyFragment fragment = new MyFragment();
        transaction.add(R.id.frame, fragment);
        transaction.commit();

        myAdapter.setMyClickListener(new MyAdapter.MyClickListener() {
            @Override
            public void onClick(View view, int y, final int position) {
                linearLayoutManager.setScrollEnabled(false);

                frameLayout.setVisibility(View.VISIBLE);
                frameLayout.setAlpha(0f);
                alphaAnimation(frameLayout, 0.0f, 1.0f, true);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) frameLayout.getLayoutParams();
                params.topMargin = y + view.getHeight() + length;
                params.height = screenHeight - view.getHeight() - length;

                int startY = 0;
                int endY = 0;
                if (view.getTranslationY() == 0f) {
                    startY = 0;
                    endY = -(y + length);
                    moveLength = endY;
                }

                ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
//                final ViewGroup content = (ViewGroup) decorView.findViewById(android.R.id.content);
                MyImageView imageView = new MyImageView(MainActivity.this);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
                    }
                });
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.mipmap.icon_bg_type_1);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getWidth(), view.getHeight());
                layoutParams.topMargin = y + 2 * length;
                decorView.addView(imageView, layoutParams);

                moveView = imageView;

                translate(imageView, startY, endY);
                translate(frameLayout, startY, endY);
            }
        });
    }

    private void translate(final View view, int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", start, end);
        animator.setDuration(1000);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (view.getTranslationY() == 0f) {
                    alphaAnimation(frameLayout, 1.0f, 0.0f, false);
                    final ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
                    for (int i = 0; i < decorView.getChildCount(); i++) {
                        if (decorView.getChildAt(i) instanceof MyImageView) {
                            decorView.removeView(decorView.getChildAt(i));
                        }
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void alphaAnimation(final View view, float fromAlpha, float toAlpha, final boolean isVisibility) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", fromAlpha, toAlpha);
        animator.setDuration(500);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isVisibility) {
                    view.setVisibility(View.GONE);
                    linearLayoutManager.setScrollEnabled(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (moveView.getTranslationY() != 0f) {
            translate(moveView, moveLength, 0);
            translate(frameLayout, moveLength, 0);
        }
    }
}
