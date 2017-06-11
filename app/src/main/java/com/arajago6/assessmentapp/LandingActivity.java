package com.arajago6.assessmentapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class LandingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        TextView txt = (TextView) findViewById(R.id.fullscreen_content);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Sansation-Bold.ttf");
        txt.setTypeface(font);

        LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("preloader.json");
        animationView.loop(true);
        animationView.playAnimation();

        RelativeLayout relLayout = (RelativeLayout) findViewById(R.id.parent_layout);
        relLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
            }

        });
    }
}
