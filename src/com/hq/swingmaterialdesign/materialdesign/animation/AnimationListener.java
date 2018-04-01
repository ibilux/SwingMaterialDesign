package com.hq.swingmaterialdesign.materialdesign.animation;

public interface AnimationListener {

    public void onStart();

    public void onAnimation(double percent);

    public void onStop();

    public void onEnd();
}
