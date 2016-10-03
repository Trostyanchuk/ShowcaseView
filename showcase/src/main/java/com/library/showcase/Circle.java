package com.library.showcase;

public class Circle {

    private int radius;
    private float alpha;

    public Circle(int radius, float alpha) {
        this.radius = radius;
        this.alpha = alpha;
    }

    public int getRadius() {
        return radius;
    }

    public Circle setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public void increaseRadius(int delta) {
        this.radius += delta;
    }

    public float getAlpha() {
        return alpha;
    }

    public Circle setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    public void decreaseAlpha(float delta) {
        this.alpha -= delta;
    }
}
