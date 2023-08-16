package com.jkqj.kernel.canal.core;

public interface LifeCycle {
    void start();

    void stopGracefully();

    boolean isStarted();
}
