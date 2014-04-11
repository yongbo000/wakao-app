package me.wakao.app.ui;


import me.wakao.app.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class WelcomeActivity extends FragmentActivity {
	private ImageView imageView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		imageView = (ImageView)findViewById(R.id.welcome);
		Animation alphaAnimation = new AlphaAnimation(0.8f, 1.0f);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				Intent intent = new Intent(WelcomeActivity.this, MainActivity2.class);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.overridePendingTransition(R.anim.slide_in_right,R.anim.scaleout);
				finish();
			}
		});
		imageView.setAnimation(alphaAnimation);
	}
}
