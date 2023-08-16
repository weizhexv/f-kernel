package com.jkqj.kernel.canal.plugin;

public abstract class AbstractPlugin implements Plugin {
    protected Plugin next;

    @Override
    public void setNext(Plugin next) {
        this.next = next;
    }

    @Override
    public Plugin getNext() {
        return next;
    }

    @Override
    public void postProcess(Context context) {

    }
}
