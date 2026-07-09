package com.ltcg.keyholder;

@FunctionalInterface
public interface KeySetter {
    void setPressed(String keyName, boolean pressed);
}
