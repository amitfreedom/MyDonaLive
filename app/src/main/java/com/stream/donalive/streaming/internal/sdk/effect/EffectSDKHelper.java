package com.stream.donalive.streaming.internal.sdk.effect;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.net.Uri;
import android.text.TextUtils;

import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyAbility;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.BlurEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.BlusherEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.BlusherMakeupEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.CheekboneSlimmingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.ChinLengtheningEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.ColoredContactsEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.DarkCirclesEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.EyelashesEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.EyelinerEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.EyesBrighteningEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.EyesEnlargingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.EyeshadowEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.FaceShorteningEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.FaceSlimmingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.FilterEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.ForeheadSlimmingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.LipstickEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.MandibleSlimmingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.MosaicEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.MouthReshapeEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.NoseLengtheningEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.NoseSlimmingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.PortraitSegmentationEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.SharpeningEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.SimpleEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.SkinToneEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.SmoothingEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.StickerEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.StyleMakeupEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.TeethWhiteningEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyEditor.WrinklesEditor;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyGroup;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyType;
import com.stream.donalive.streaming.internal.sdk.effect.net.APIBase;
import com.stream.donalive.streaming.internal.sdk.effect.net.IGetLicenseCallback;
import com.stream.donalive.streaming.internal.sdk.effect.net.License;
import com.stream.donalive.streaming.internal.utils.ZegoUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import im.zego.effects.ZegoEffects;

public class EffectSDKHelper {

    private static String portraitSegmentationImagePath = "BackgroundImages/image1.jpg";

    public static void setResources(Context context, String rootFolder, String resourceFolderName) {
        boolean assetFileExists = isAssetFileExists(context, resourceFolderName);
        String resourceRootFolder = rootFolder + File.separator + resourceFolderName;
        File file = new File(resourceRootFolder);
        if (assetFileExists && !file.exists()) {
            ZegoUtil.copyFileFromAssets(context, resourceFolderName, resourceRootFolder);
        }

        ArrayList<String> resources = new ArrayList<>();

        String faceDetectionModel = resourceRootFolder + File.separator + "FaceDetection.model";
        String commonResources = resourceRootFolder + File.separator + "CommonResources.bundle";
        String faceWhiteningResources = commonResources + File.separator + "FaceWhiteningResources";
        String rosyResources = commonResources + File.separator + "RosyResources";
        String teethWhiteningResources = commonResources + File.separator + "TeethWhiteningResources";
        String stickerPath = resourceRootFolder + File.separator + "StickerBaseResources.bundle";
        String segmentationPath = resourceRootFolder + File.separator + "BackgroundSegmentation.model";

        resources.add(faceDetectionModel);
        resources.add(commonResources);
        resources.add(faceWhiteningResources);
        resources.add(rosyResources);
        resources.add(teethWhiteningResources);
        resources.add(stickerPath);
        resources.add(segmentationPath);
        ZegoEffects.setResources(resources);
    }

    private static boolean isAssetFileExists(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] files = assetManager.list("");
            for (String file : files) {
                if (file.equals(fileName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void getLicence(Context context, String baseUrl, long appID, String appSign,
        IGetLicenseCallback callback) {
        SharedPreferences sp = context.getSharedPreferences("effect", Context.MODE_PRIVATE);
        if (sp.contains("licence")) {
            long timestamp = sp.getLong("timestamp", 0L);
            String string = sp.getString("licence", "");
            if (System.currentTimeMillis() - timestamp < 24 * 60 * 60 * 1000) {
                License license = new License();
                license.setLicense(string);
                if (callback != null) {
                    callback.onGetLicense(0, "success from local", license);
                }
                return;
            }
        }

        String authInfo = ZegoEffects.getAuthInfo(appSign, context);
        Uri.Builder builder = Uri.parse(baseUrl).buildUpon();
        builder.appendQueryParameter("AppId", String.valueOf(appID));
        builder.appendQueryParameter("AuthInfo", authInfo);
        String url = builder.build().toString();

        APIBase.asyncGet(url, License.class, (code, message, responseJsonBean) -> {
            if (code == 0) {
                Editor edit = sp.edit();
                edit.putString("licence", responseJsonBean.getLicense());
                edit.putLong("timestamp", System.currentTimeMillis());
                edit.apply();
            }
            if (callback != null) {
                callback.onGetLicense(code, message, responseJsonBean);
            }
        });
    }

    public static Map<BeautyGroup, List<BeautyAbility>> getAllAbilities() {
        Map<BeautyGroup, List<BeautyAbility>> results = new HashMap<>();
        results.put(BeautyGroup.BASIC, getBeautyBasics());
        results.put(BeautyGroup.ADVANCED, getBeautyAdvanced());
        results.put(BeautyGroup.LIPSTICK, getBeautyMakeupLipsticks());
        results.put(BeautyGroup.BLUSHER, getBeautyMakeupBlushers());
        results.put(BeautyGroup.EYELASHES, getBeautyMakeupEyelashes());
        results.put(BeautyGroup.EYELINER, getBeautyMakeupEyeliners());
        results.put(BeautyGroup.EYESHADOW, getBeautyMakeupEyeshadows());
        results.put(BeautyGroup.COLORED_CONTACTS, getBeautyMakeupColoredContact());
        results.put(BeautyGroup.STYLE_MAKEUP, getBeautyStyleMakeUps());
        results.put(BeautyGroup.FILTERS, getFilters());
        results.put(BeautyGroup.STICKERS, getStickers());
        results.put(BeautyGroup.BACKGROUND, getBackgrounds());
        return results;
    }

    public static List<BeautyAbility> getBeautyBasics() {
        List<BeautyAbility> basics = new ArrayList<>();
        basics.add(new BeautyAbility(BeautyType.BASIC_RESET, BeautyGroup.BASIC, 0, 0, 0, new SimpleEditor()));
        basics.add(new BeautyAbility(BeautyType.BASIC_SMOOTHING, BeautyGroup.BASIC, new SmoothingEditor()));
        basics.add(new BeautyAbility(BeautyType.BASIC_SKIN_TONE, BeautyGroup.BASIC, new SkinToneEditor()));
        basics.add(new BeautyAbility(BeautyType.BASIC_BLUSHER, BeautyGroup.BASIC, new BlusherEditor()));
        basics.add(new BeautyAbility(BeautyType.BASIC_SHARPENING, BeautyGroup.BASIC, new SharpeningEditor()));
        basics.add(new BeautyAbility(BeautyType.BASIC_WRINKLES, BeautyGroup.BASIC, new WrinklesEditor()));
        basics.add(new BeautyAbility(BeautyType.BASIC_DARK_CIRCLES, BeautyGroup.BASIC, new DarkCirclesEditor()));
        return basics;
    }

    public static List<BeautyAbility> getBeautyAdvanced() {
        List<BeautyAbility> advanced = new ArrayList<>();
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_RESET, BeautyGroup.ADVANCED, 0, 0, 0, new SimpleEditor()));
        advanced.add(
            new BeautyAbility(BeautyType.ADVANCED_FACE_SLIMMING, BeautyGroup.ADVANCED, new FaceSlimmingEditor()));
        advanced.add(
            new BeautyAbility(BeautyType.ADVANCED_EYES_ENLARGING, BeautyGroup.ADVANCED, new EyesEnlargingEditor()));
        advanced.add(
            new BeautyAbility(BeautyType.ADVANCED_EYES_BRIGHTENING, BeautyGroup.ADVANCED, new EyesBrighteningEditor()));
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_CHIN_LENGTHENING, BeautyGroup.ADVANCED, -100, 100, 50,
            new ChinLengtheningEditor()));
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_MOUTH_RESHAPE, BeautyGroup.ADVANCED, -100, 100, 50,
            new MouthReshapeEditor()));
        advanced.add(
            new BeautyAbility(BeautyType.ADVANCED_TEETH_WHITENING, BeautyGroup.ADVANCED, new TeethWhiteningEditor()));
        advanced.add(
            new BeautyAbility(BeautyType.ADVANCED_NOSE_SLIMMING, BeautyGroup.ADVANCED, new NoseSlimmingEditor()));
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_NOSE_LENGTHENING, BeautyGroup.ADVANCED, -100, 100, 50,
            new NoseLengtheningEditor()));
        advanced.add(
            new BeautyAbility(BeautyType.ADVANCED_FACE_SHORTENING, BeautyGroup.ADVANCED, new FaceShorteningEditor()));
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_MANDIBLE_SLIMMING, BeautyGroup.ADVANCED,
            new MandibleSlimmingEditor()));
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_CHEEKBONE_SLIMMING, BeautyGroup.ADVANCED,
            new CheekboneSlimmingEditor()));
        advanced.add(new BeautyAbility(BeautyType.ADVANCED_FOREHEAD_SLIMMING, BeautyGroup.ADVANCED, -100, 100, 50,
            new ForeheadSlimmingEditor()));
        return advanced;
    }

    public static List<BeautyAbility> getBeautyMakeupLipsticks() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.MAKEUP_LIPSTICK_NONE,
            BeautyType.MAKEUP_LIPSTICK_CAMEO_PINK, BeautyType.MAKEUP_LIPSTICK_SWEET_ORANGE,
            BeautyType.MAKEUP_LIPSTICK_RUST_RED, BeautyType.MAKEUP_LIPSTICK_CORAL,
            BeautyType.MAKEUP_LIPSTICK_RED_VELVET);
        List<BeautyAbility> lipsticks = new ArrayList<>();
        for (BeautyType type : beautyTypes) {
            lipsticks.add(new BeautyAbility(type, BeautyGroup.LIPSTICK, new LipstickEditor(getResourcePath(type))));
        }
        return lipsticks;
    }

    public static List<BeautyAbility> getBeautyMakeupBlushers() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.MAKEUP_BLUSHER_NONE,
            BeautyType.MAKEUP_BLUSHER_SLIGHTLY_DRUNK, BeautyType.MAKEUP_BLUSHER_PEACH,
            BeautyType.MAKEUP_BLUSHER_MILKY_ORANGE, BeautyType.MAKEUP_BLUSHER_APRICOT_PINK,
            BeautyType.MAKEUP_BLUSHER_SWEET_ORANGE);
        List<BeautyAbility> blushers = new ArrayList<>();
        for (BeautyType type : beautyTypes) {
            blushers.add(new BeautyAbility(type, BeautyGroup.BLUSHER, new BlusherMakeupEditor(getResourcePath(type))));
        }
        return blushers;
    }

    public static List<BeautyAbility> getBeautyMakeupEyelashes() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.MAKEUP_EYELASHES_NONE,
            BeautyType.MAKEUP_EYELASHES_NATURAL, BeautyType.MAKEUP_EYELASHES_TENDER, BeautyType.MAKEUP_EYELASHES_CURL,
            BeautyType.MAKEUP_EYELASHES_EVERLONG, BeautyType.MAKEUP_EYELASHES_THICK);
        List<BeautyAbility> eyelashes = new ArrayList<>();
        for (BeautyType type : beautyTypes) {
            eyelashes.add(new BeautyAbility(type, BeautyGroup.EYELASHES, new EyelashesEditor(getResourcePath(type))));
        }
        return eyelashes;
    }

    public static List<BeautyAbility> getBeautyMakeupEyeliners() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.MAKEUP_EYELINER_NONE,
            BeautyType.MAKEUP_EYELINER_NATURAL, BeautyType.MAKEUP_EYELINER_CAT_EYE, BeautyType.MAKEUP_EYELINER_NAUGHTY,
            BeautyType.MAKEUP_EYELINER_INNOCENT, BeautyType.MAKEUP_EYELINER_DIGNIFIED);
        List<BeautyAbility> beauties = new ArrayList<>();
        for (BeautyType beautyType : beautyTypes) {
            beauties.add(
                new BeautyAbility(beautyType, BeautyGroup.EYELINER, new EyelinerEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyAbility> getBeautyMakeupEyeshadows() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.MAKEUP_EYESHADOW_NONE,
            BeautyType.MAKEUP_EYESHADOW_PINK_MIST, BeautyType.MAKEUP_EYESHADOW_SHIMMER_PINK,
            BeautyType.MAKEUP_EYESHADOW_TEA_BROWN, BeautyType.MAKEUP_EYESHADOW_BRIGHT_ORANGE,
            BeautyType.MAKEUP_EYESHADOW_MOCHA_BROWN);
        List<BeautyAbility> beauties = new ArrayList<>();
        for (BeautyType beautyType : beautyTypes) {
            beauties.add(
                new BeautyAbility(beautyType, BeautyGroup.EYESHADOW, new EyeshadowEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyAbility> getBeautyMakeupColoredContact() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.MAKEUP_COLORED_CONTACTS_NONE,
            BeautyType.MAKEUP_COLORED_CONTACTS_DARKNIGHT_BLACK, BeautyType.MAKEUP_COLORED_CONTACTS_STARRY_BLUE,
            BeautyType.MAKEUP_COLORED_CONTACTS_BROWN_GREEN, BeautyType.MAKEUP_COLORED_CONTACTS_LIGHTS_BROWN,
            BeautyType.MAKEUP_COLORED_CONTACTS_CHOCOLATE_BROWN);
        List<BeautyAbility> beauties = new ArrayList<>();
        for (BeautyType beautyType : beautyTypes) {
            beauties.add(new BeautyAbility(beautyType, BeautyGroup.COLORED_CONTACTS,
                new ColoredContactsEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyAbility> getBeautyStyleMakeUps() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.STYLE_MAKEUP_NONE,
            BeautyType.STYLE_MAKEUP_INNOCENT_EYES, BeautyType.STYLE_MAKEUP_MILKY_EYES,
            BeautyType.STYLE_MAKEUP_CUTIE_COOL, BeautyType.STYLE_MAKEUP_PURE_SEXY, BeautyType.STYLE_MAKEUP_FLAWLESS);
        List<BeautyAbility> beauties = new ArrayList<>();
        for (BeautyType beautyType : beautyTypes) {
            beauties.add(new BeautyAbility(beautyType, BeautyGroup.STYLE_MAKEUP,
                new StyleMakeupEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyAbility> getFilters() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.FILTER_NONE, BeautyType.FILTER_NATURAL_CREAMY,
            BeautyType.FILTER_NATURAL_BRIGHTEN, BeautyType.FILTER_NATURAL_FRESH, BeautyType.FILTER_NATURAL_AUTUMN,
            BeautyType.FILTER_GRAY_MONET, BeautyType.FILTER_GRAY_NIGHT, BeautyType.FILTER_GRAY_FILMLIKE,
            BeautyType.FILTER_DREAMY_SUNSET, BeautyType.FILTER_DREAMY_COZILY, BeautyType.FILTER_DREAMY_SWEET);
        List<BeautyAbility> beauties = new ArrayList<>();
        for (BeautyType beautyType : beautyTypes) {
            beauties.add(
                new BeautyAbility(beautyType, BeautyGroup.FILTERS, new FilterEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyAbility> getStickers() {
        List<BeautyType> beautyTypes = Arrays.asList(BeautyType.STICKER_NONE, BeautyType.STICKER_ANIMAL,
            BeautyType.STICKER_DIVE, BeautyType.STICKER_CAT, BeautyType.STICKER_WATERMELON, BeautyType.STICKER_DEER,
            BeautyType.STICKER_COOL_GIRL, BeautyType.STICKER_CLOWN, BeautyType.STICKER_CLAW_MACHINE,
            BeautyType.STICKER_SAILOR_MOON);
        List<BeautyAbility> beauties = new ArrayList<>();
        for (BeautyType beautyType : beautyTypes) {
            beauties.add(
                new BeautyAbility(beautyType, BeautyGroup.STICKERS, new StickerEditor(getResourcePath(beautyType))));
        }
        return beauties;
    }

    public static List<BeautyAbility> getBackgrounds() {
        List<BeautyAbility> background = new ArrayList<>();
        background.add(
            new BeautyAbility(BeautyType.BACKGROUND_NONE, BeautyGroup.BACKGROUND, 0, 0, 0, new SimpleEditor()));
        background.add(
            new BeautyAbility(BeautyType.BACKGROUND_PORTRAIT_SEGMENTATION, BeautyGroup.BACKGROUND, 100, 100, 100,
                new PortraitSegmentationEditor(
                    ZegoEffectsService.resourceRootFolder + File.separator + portraitSegmentationImagePath)));
        background.add(new BeautyAbility(BeautyType.BACKGROUND_MOSAICING, BeautyGroup.BACKGROUND, new MosaicEditor()));
        background.add(
            new BeautyAbility(BeautyType.BACKGROUND_GAUSSIAN_BLUR, BeautyGroup.BACKGROUND, new BlurEditor()));
        return background;
    }

    private static String getResourcePath(BeautyType beautyType) {
        if (beautyType.name().endsWith("_RESET") || beautyType.name().endsWith("_NONE")) {
            return "";
        }
        String bundleName;
        if (beautyType.name().startsWith("BASIC_") || beautyType.name().startsWith("ADVANCED_") || beautyType.name()
            .startsWith("MAKEUP_") || beautyType.name().startsWith("STYLE_")) {
            bundleName = toCamelCase("beauty" + beautyType.name(), true, '_');
        } else {
            bundleName = toCamelCase(beautyType.name(), true, '_');
        }
        return ZegoEffectsService.resourceRootFolder + File.separator + "AdvancedResources" + File.separator
            + bundleName + ".bundle";
    }

    public static String toCamelCase(String str, final boolean capitalizeFirstLetter, final char... delimiters) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        str = str.toLowerCase();
        final int strLen = str.length();
        final int[] newCodePoints = new int[strLen];
        int outOffset = 0;
        final Set<Integer> delimiterSet = toDelimiterSet(delimiters);
        boolean capitalizeNext = capitalizeFirstLetter;
        for (int index = 0; index < strLen; ) {
            final int codePoint = str.codePointAt(index);

            if (delimiterSet.contains(codePoint)) {
                capitalizeNext = outOffset != 0;
                index += Character.charCount(codePoint);
            } else if (capitalizeNext || outOffset == 0 && capitalizeFirstLetter) {
                final int titleCaseCodePoint = Character.toTitleCase(codePoint);
                newCodePoints[outOffset++] = titleCaseCodePoint;
                index += Character.charCount(titleCaseCodePoint);
                capitalizeNext = false;
            } else {
                newCodePoints[outOffset++] = codePoint;
                index += Character.charCount(codePoint);
            }
        }

        return new String(newCodePoints, 0, outOffset);
    }

    private static Set<Integer> toDelimiterSet(final char[] delimiters) {
        final Set<Integer> delimiterHashSet = new HashSet<>();
        delimiterHashSet.add(Character.codePointAt(new char[]{' '}, 0));
        if (delimiters == null || delimiters.length == 0) {
            return delimiterHashSet;
        }

        for (int index = 0; index < delimiters.length; index++) {
            delimiterHashSet.add(Character.codePointAt(delimiters, index));
        }
        return delimiterHashSet;
    }
}
