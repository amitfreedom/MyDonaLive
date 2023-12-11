package com.stream.donalive.streaming.internal.sdk.effect.bean;

import java.util.Collections;
import java.util.List;

/**
 * beauty:  1. enable function  2.set param filter:  1. setFilter path 2. set param
 */
public class BeautyAbility {

    private int maxValue;
    private int minValue;
    private int defaultValue;
    private List<BeautyType> beautyTypes;
    private BeautyEditor editor;
    private BeautyGroup beautyGroup;

    public BeautyAbility(List<BeautyType> beautyTypes, BeautyGroup beautyGroup, int minValue, int maxValue,
        int defaultValue, BeautyEditor editor) {
        this.beautyGroup = beautyGroup;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        this.editor = editor;
        this.beautyTypes = beautyTypes;
    }

    public BeautyAbility(BeautyType beautyTypes, BeautyGroup beautyGroup, int minValue, int maxValue, int defaultValue,
        BeautyEditor editor) {
        this.beautyGroup = beautyGroup;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.defaultValue = defaultValue;
        this.editor = editor;
        this.beautyTypes = Collections.singletonList(beautyTypes);
    }

    public BeautyAbility(BeautyType beautyTypes, BeautyGroup beautyGroup, BeautyEditor editor) {
        this.beautyGroup = beautyGroup;
        this.maxValue = 100;
        this.minValue = 0;
        this.defaultValue = 50;
        this.editor = editor;
        this.beautyTypes = Collections.singletonList(beautyTypes);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public BeautyGroup getBeautyGroup() {
        return beautyGroup;
    }

    public List<BeautyType> getBeautyTypes() {
        return beautyTypes;
    }

    public boolean contains(BeautyType beautyType) {
        return beautyTypes.contains(beautyType);
    }

    public BeautyEditor getEditor() {
        return editor;
    }

    @Override
    public String toString() {
        return "BeautyAbility{" + "maxValue=" + maxValue + ", minValue=" + minValue + ", defaultValue=" + defaultValue
            + ", beautyTypes=" + beautyTypes + ", beautyGroup=" + beautyGroup + '}';
    }
}
