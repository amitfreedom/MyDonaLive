package com.stream.donalive.streaming.internal.sdk.effect;

import android.content.Context;

import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyAbility;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyGroup;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyType;
import com.stream.donalive.streaming.internal.sdk.effect.net.IGetLicenseCallback;
import com.stream.donalive.streaming.internal.sdk.effect.net.License;
import com.stream.donalive.streaming.internal.utils.LogUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.zego.effects.ZegoEffects;
import im.zego.effects.callback.ZegoEffectsEventHandler;
import im.zego.effects.entity.ZegoEffectsFaceDetectionResult;
import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.enums.ZegoEffectsScaleMode;

public class ZegoEffectsService {

    public final static String BACKEND_API_URL = "https://aieffects-api.zego.im/?Action=DescribeEffectsLicense";

    private ZegoEffects zegoEffects;

    private List<BeautyGroup> groupConfigs = Arrays.asList(BeautyGroup.BASIC, BeautyGroup.ADVANCED, BeautyGroup.FILTERS,
        BeautyGroup.LIPSTICK, BeautyGroup.BLUSHER, BeautyGroup.EYELASHES, BeautyGroup.EYELINER, BeautyGroup.EYESHADOW,
        BeautyGroup.COLORED_CONTACTS, BeautyGroup.STYLE_MAKEUP, BeautyGroup.STICKERS, BeautyGroup.BACKGROUND);

    private Map<BeautyGroup, List<BeautyAbility>> beautyAbilities = new HashMap<>();
    private Map<BeautyType, Integer> beautyParams = new HashMap<>();

    public static String resourceRootFolder = "";

    public void init(Context context, long appID, String appSign, IGetLicenseCallback callback) {

        String cacheDir = context.getExternalFilesDir(null).getPath();
        String resourceFolderName = "BeautyResources";
        resourceRootFolder = cacheDir + File.separator + resourceFolderName;
        // step 1, setResources
        EffectSDKHelper.setResources(context, cacheDir, resourceFolderName);

        // step 2, getLicence
        EffectSDKHelper.getLicence(context, BACKEND_API_URL, appID, appSign, new IGetLicenseCallback() {
            @Override
            public void onGetLicense(int code, String message, License license) {
                LogUtil.d("onGetLicense() called with: code = [" + code + "], message = [" + message + "], license = ["
                    + license + "]");
                if (code == 0) {
                    // step 3 create effect
                    zegoEffects = ZegoEffects.create(license.getLicense(), context);
                    zegoEffects.enableFaceDetection(true);
                    zegoEffects.setEventHandler(new ZegoEffectsEventHandler() {
                        @Override
                        public void onError(ZegoEffects effects, int errorCode, String desc) {
                            super.onError(effects, errorCode, desc);
                            LogUtil.d("errorCode:" + errorCode);
                        }

                        @Override
                        public void onFaceDetectionResult(ZegoEffectsFaceDetectionResult[] results,
                            ZegoEffects effects) {
                            for (ZegoEffectsFaceDetectionResult result : results) {
                                LogUtil.d("onFaceDetectionResult,results.size:" + results.length + ",rect : point("
                                    + result.rect.x + "," + result.rect.y + "),width:" + result.rect.width + ",height:"
                                    + result.rect.height + ",getScore: " + result.getScore());
                            }
                        }
                    });
                }
                // step 4 enableAbilities
                beautyAbilities = EffectSDKHelper.getAllAbilities();
                if (callback != null) {
                    callback.onGetLicense(code, message, license);
                }
            }
        });
    }


    public void resetBeautyBasics() {
        List<BeautyAbility> abilities = beautyAbilities.get(BeautyGroup.BASIC);
        for (BeautyAbility ability : abilities) {
            BeautyEditor editor = ability.getEditor();
            editor.apply(ability.getDefaultValue());
            for (BeautyType beautyType : ability.getBeautyTypes()) {
                beautyParams.put(beautyType, ability.getDefaultValue());
            }
        }
    }

    public void resetBeautyAdvanced() {
        List<BeautyAbility> abilities = beautyAbilities.get(BeautyGroup.ADVANCED);
        for (BeautyAbility ability : abilities) {
            BeautyEditor editor = ability.getEditor();
            editor.apply(ability.getDefaultValue());
            for (BeautyType beautyType : ability.getBeautyTypes()) {
                beautyParams.put(beautyType, ability.getDefaultValue());
            }
        }
    }

    public void removeBackgrounds() {
        zegoEffects.setPortraitSegmentationBackgroundPath(null, ZegoEffectsScaleMode.ASPECT_FILL);
        zegoEffects.enablePortraitSegmentation(false);
        zegoEffects.enablePortraitSegmentationBackground(false);
        zegoEffects.enablePortraitSegmentationBackgroundMosaic(false);
        zegoEffects.enablePortraitSegmentationBackgroundBlur(false);
    }

    public List<BeautyGroup> getGroupConfigs() {
        return groupConfigs;
    }

    public BeautyAbility getBeautyAbility(BeautyType beautyType) {
        for (List<BeautyAbility> value : beautyAbilities.values()) {
            for (BeautyAbility ability : value) {
                if (ability.contains(beautyType)) {
                    return ability;
                }
            }
        }
        return null;
    }

    public void enableBeauty(BeautyType beautyType, boolean enable) {
        if (zegoEffects == null) {
            return;
        }
        BeautyAbility beautyAbility = getBeautyAbility(beautyType);
        beautyAbility.getEditor().enable(enable);
    }

    public void setBeautyValue(BeautyType beautyType, int value) {
        if (zegoEffects == null) {
            return;
        }
        BeautyAbility beautyAbility = getBeautyAbility(beautyType);
        beautyAbility.getEditor().apply(value);
        beautyParams.put(beautyType, value);
    }

    public int getBeautyValue(BeautyType beautyType) {
        Integer integer = beautyParams.get(beautyType);
        if (integer == null) {
            BeautyAbility beautyAbility = getBeautyAbility(beautyType);
            return beautyAbility.getDefaultValue();
        }
        return integer;
    }

    public void resetBeautyValue(BeautyType beautyType) {
        BeautyAbility beautyAbility = getBeautyAbility(beautyType);
        BeautyEditor editor = beautyAbility.getEditor();
        editor.apply(beautyAbility.getDefaultValue());
    }

    public void initEnv(int captureWidth, int captureHeight) {
        if (zegoEffects == null) {
            return;
        }
        zegoEffects.initEnv(captureWidth, captureHeight);
    }

    public void uninitEnv() {
        if (zegoEffects == null) {
            return;
        }
        zegoEffects.uninitEnv();
    }

    public boolean isEffectSDKInit() {
        return zegoEffects != null;
    }

    public ZegoEffects getEffectSDK() {
        return zegoEffects;
    }

    public int processTexture(int textureID, ZegoEffectsVideoFrameParam effectsVideoFrameParam) {
        if (zegoEffects == null) {
            return textureID;
        }
        return zegoEffects.processTexture(textureID, effectsVideoFrameParam);
    }

    public void resetAndDisableAllAbilities() {
        if (zegoEffects != null) {
            for (List<BeautyAbility> value : beautyAbilities.values()) {
                for (BeautyAbility beautyAbility : value) {
                    BeautyEditor editor = beautyAbility.getEditor();
                    editor.apply(beautyAbility.getDefaultValue());
                    editor.enable(false);
                }
            }
            beautyParams.clear();
        }
    }
}
