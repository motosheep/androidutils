package com.north.light.androidutils.recyclerview.anim;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class ScaleInAnimation {
    private static final float DEFAULT_SCALE_FROM = 0.7f;
    private final float mFrom;

    public ScaleInAnimation() {
        this(DEFAULT_SCALE_FROM);
    }

    public ScaleInAnimation(float from) {
        mFrom = from;
    }

    public Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1f);
        return new ObjectAnimator[]{scaleX, scaleY, alpha};
    }
}
