package com.library.showcase;

class Circle {

    private int radius;
    private float alpha;

    Circle(int radius, float alpha) {
        this.radius = radius;
        this.alpha = alpha;
    }

    int getRadius() {
        return radius;
    }

    public Circle setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    void increaseRadius(int delta) {
        this.radius += delta;
    }

    public float getAlpha() {
        return alpha;
    }

    public Circle setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    void decreaseAlpha(float delta) {
        this.alpha -= delta;
    }
}
