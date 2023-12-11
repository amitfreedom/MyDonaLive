package com.stream.donalive.streaming.internal.sdk;

import android.app.Application;
import android.content.Context;

import com.stream.donalive.streaming.internal.sdk.basic.MergeCallBack;
import com.stream.donalive.streaming.internal.sdk.basic.ZEGOSDKCallBack;
import com.stream.donalive.streaming.internal.sdk.effect.ZegoEffectsService;
import com.stream.donalive.streaming.internal.sdk.effect.net.IGetLicenseCallback;
import com.stream.donalive.streaming.internal.sdk.effect.net.License;
import com.stream.donalive.streaming.internal.sdk.express.ExpressService;
import com.stream.donalive.streaming.internal.sdk.zim.ZIMService;
import com.stream.donalive.streaming.internal.utils.LogUtil;

import org.json.JSONObject;

import im.zego.effects.entity.ZegoEffectsVideoFrameParam;
import im.zego.effects.enums.ZegoEffectsVideoFrameFormat;
import im.zego.zegoexpress.callback.IZegoCustomVideoProcessHandler;
import im.zego.zegoexpress.callback.IZegoRoomLoginCallback;
import im.zego.zegoexpress.callback.IZegoRoomLogoutCallback;
import im.zego.zegoexpress.callback.IZegoUploadLogResultCallback;
import im.zego.zegoexpress.constants.ZegoPublishChannel;
import im.zego.zegoexpress.constants.ZegoScenario;
import im.zego.zegoexpress.constants.ZegoVideoBufferType;
import im.zego.zegoexpress.entity.ZegoCustomVideoProcessConfig;
import im.zego.zegoexpress.entity.ZegoVideoConfig;
import im.zego.zim.callback.ZIMLogUploadedCallback;
import im.zego.zim.callback.ZIMLoggedInCallback;
import im.zego.zim.callback.ZIMRoomEnteredCallback;
import im.zego.zim.callback.ZIMRoomLeftCallback;
import im.zego.zim.entity.ZIMError;
import im.zego.zim.entity.ZIMRoomFullInfo;
import im.zego.zim.enums.ZIMErrorCode;

public class ZEGOSDKManager {

    public ExpressService expressService = new ExpressService();
    public ZIMService zimService = new ZIMService();
    public ZegoEffectsService effectsService = new ZegoEffectsService();
    private Context context;
    private long appID;
    private String appSign;

    private ZEGOSDKManager() {

    }

    private static final class Holder {

        private static final ZEGOSDKManager INSTANCE = new ZEGOSDKManager();
    }

    public static ZEGOSDKManager getInstance() {
        return Holder.INSTANCE;
    }

    public void initSDK(Application application, long appID, String appSign) {
        initSDK(application, appID, appSign, ZegoScenario.DEFAULT);
    }

    public void initSDK(Application application, long appID, String appSign, ZegoScenario scenario) {
        expressService.initSDK(application, appID, appSign, scenario);
        zimService.initSDK(application, appID, appSign);
        context = application.getApplicationContext();
        this.appID = appID;
        this.appSign = appSign;
    }

    public void enableZEGOEffects(boolean enable) {
        if (enable) {
            effectsService.init(context, appID, appSign, new IGetLicenseCallback() {
                @Override
                public void onGetLicense(int code, String message, License license) {
                    LogUtil.d(
                        "onGetLicense() called with: code = [" + code + "], message = [" + message + "], license = ["
                            + license + "]");
                    enableCustomVideoProcess(code == 0);
                }
            });
        } else {
            enableCustomVideoProcess(false);
        }
    }

    private void enableCustomVideoProcess(boolean enable) {
        if (enable) {
            ZegoCustomVideoProcessConfig config = new ZegoCustomVideoProcessConfig();
            config.bufferType = ZegoVideoBufferType.GL_TEXTURE_2D;

            expressService.enableCustomVideoProcessing(true, config, ZegoPublishChannel.MAIN);
            expressService.setCustomVideoProcessHandler(new IZegoCustomVideoProcessHandler() {
                @Override
                public void onStart(ZegoPublishChannel channel) {
                    LogUtil.d("[Express] [onStart]");
                    effectsService.uninitEnv();
                    ZegoVideoConfig videoConfig = expressService.getVideoConfig();
                    effectsService.initEnv(videoConfig.captureWidth, videoConfig.captureHeight);

                }

                @Override
                public void onStop(ZegoPublishChannel channel) {
                    LogUtil.d("[Express] [onStop]");
                    effectsService.uninitEnv();
                }

                @Override
                public void onCapturedUnprocessedTextureData(int textureID, int width, int height,
                    long referenceTimeMillisecond, ZegoPublishChannel channel) {
                    //                Log.i("ZEGO",
                    //                    "[Express] [onCapturedUnprocessedTextureData] textureID: " + textureID + ", width: " + width
                    //                        + ", height: " + height + ", ts: " + referenceTimeMillisecond);
                    // Receive texture from ZegoExpressEngine

                    ZegoEffectsVideoFrameParam effectsVideoFrameParam = new ZegoEffectsVideoFrameParam();
                    effectsVideoFrameParam.format = ZegoEffectsVideoFrameFormat.RGBA32;
                    effectsVideoFrameParam.width = width;
                    effectsVideoFrameParam.height = height;

                    // Process buffer by ZegoEffects
                    int processedTextureID = effectsService.processTexture(textureID, effectsVideoFrameParam);

                    // Send processed texture to ZegoExpressEngine
                    expressService.sendCustomVideoProcessedTextureData(processedTextureID, width, height,
                        referenceTimeMillisecond);
                }
            });
        } else {
            expressService.enableCustomVideoProcessing(true, null, ZegoPublishChannel.MAIN);
        }

    }

    public void connectUser(String userID, String userName, ZEGOSDKCallBack callback) {
        expressService.connectUser(userID, userName);
        zimService.connectUser(userID, userName, new ZIMLoggedInCallback() {
            @Override
            public void onLoggedIn(ZIMError errorInfo) {
                if (callback != null) {
                    callback.onResult(errorInfo.code.value(), errorInfo.message);
                }
            }
        });
    }

    public void connectUser(String userID, String userName, String token, ZEGOSDKCallBack callback) {
        expressService.connectUser(userID, userName);
        zimService.connectUser(userID, userName, token, new ZIMLoggedInCallback() {
            @Override
            public void onLoggedIn(ZIMError errorInfo) {
                if (callback != null) {
                    callback.onResult(errorInfo.code.value(), errorInfo.message);
                }
            }
        });
    }

    public void disconnectUser() {
        zimService.logoutRoom(null);
        expressService.logoutRoom(null);
        zimService.disconnectUser();
        expressService.disconnectUser();

    }

    public void loginRoom(String roomID, ZegoScenario scenario, ZEGOSDKCallBack callback) {
        zimService.loginRoom(roomID, new ZIMRoomEnteredCallback() {
            @Override
            public void onRoomEntered(ZIMRoomFullInfo roomInfo, ZIMError errorInfo) {
                if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                    expressService.setRoomScenario(scenario);
                    expressService.loginRoom(roomID, new IZegoRoomLoginCallback() {
                        @Override
                        public void onRoomLoginResult(int errorCode, JSONObject extendedData) {
                            if (callback != null) {
                                callback.onResult(errorCode, "express error:" + extendedData.toString());
                            }
                        }
                    });
                } else {
                    if (callback != null) {
                        callback.onResult(errorInfo.code.value(), "zim error:" + errorInfo.message);
                    }
                }
            }
        });
    }

    public void loginRoom(String roomID, String token, ZegoScenario scenario, ZEGOSDKCallBack callback) {
        zimService.loginRoom(roomID, new ZIMRoomEnteredCallback() {
            @Override
            public void onRoomEntered(ZIMRoomFullInfo roomInfo, ZIMError errorInfo) {
                if (errorInfo.code == ZIMErrorCode.SUCCESS) {
                    expressService.setRoomScenario(scenario);
                    expressService.loginRoom(roomID, token, new IZegoRoomLoginCallback() {
                        @Override
                        public void onRoomLoginResult(int errorCode, JSONObject extendedData) {
                            if (callback != null) {
                                callback.onResult(errorCode, "express error:" + extendedData.toString());
                            }
                        }
                    });
                } else {
                    if (callback != null) {
                        callback.onResult(errorInfo.code.value(), "zim error:" + errorInfo.message);
                    }
                }
            }
        });
    }

    public void logoutRoom(ZEGOSDKCallBack callBack) {
        MergeCallBack<Integer, ZIMError> mergeCallBack = new MergeCallBack<Integer, ZIMError>() {
            @Override
            public void onResult(Integer integer, ZIMError zimError) {
                int errorCode;
                String errorMessage = "";
                if (integer == 0) {
                    errorMessage = zimError.message;
                    errorCode = zimError.code.value();
                } else {
                    errorCode = integer;
                }
                if (callBack != null) {
                    callBack.onResult(errorCode, errorMessage);
                }
            }
        };

        expressService.logoutRoom(new IZegoRoomLogoutCallback() {
            @Override
            public void onRoomLogoutResult(int errorCode, JSONObject extendedData) {
                mergeCallBack.setResult1(errorCode);
            }
        });
        zimService.logoutRoom(new ZIMRoomLeftCallback() {
            @Override
            public void onRoomLeft(String roomID, ZIMError errorInfo) {
                mergeCallBack.setResult2(errorInfo);
            }
        });
        effectsService.resetAndDisableAllAbilities();
    }

    public void uploadLog(ZEGOSDKCallBack callBack) {

        MergeCallBack<Integer, ZIMError> mergeCallBack = new MergeCallBack<Integer, ZIMError>() {
            @Override
            public void onResult(Integer integer, ZIMError zimError) {
                int errorCode;
                String errorMessage = "";
                if (integer == 0) {
                    errorMessage = zimError.message;
                    errorCode = zimError.code.value();
                } else {
                    errorCode = integer;
                }
                if (callBack != null) {
                    callBack.onResult(errorCode, errorMessage);
                }
            }
        };
        expressService.uploadLog(new IZegoUploadLogResultCallback() {

            @Override
            public void onUploadLogResult(int errorCode) {
                mergeCallBack.setResult1(errorCode);
            }
        });

        zimService.uploadLog(new ZIMLogUploadedCallback() {

            @Override
            public void onLogUploaded(ZIMError errorInfo) {
                mergeCallBack.setResult2(errorInfo);
            }
        });
    }

    public void setDebugMode(boolean debugMode) {
        LogUtil.setDebug(debugMode);
    }
}
