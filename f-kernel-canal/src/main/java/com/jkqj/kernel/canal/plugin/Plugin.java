package com.jkqj.kernel.canal.plugin;

public interface Plugin extends Comparable<Plugin> {
    PluginType getType();

    void doProcess(Context context);

    void postProcess(Context context);

    default void process(Context context) {
        doProcess(context);

        if (getNext() != null) {
            getNext().process(context);
        }

        postProcess(context);
    }

    void setNext(Plugin plugin);

    Plugin getNext();

    @Override
    default int compareTo(Plugin that) {
        return this.getType().ordinal() - that.getType().ordinal();
    }
}
