package com.stream.donalive.streaming.internal.sdk.components.effect;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener;
import com.google.android.material.tabs.TabLayout.Tab;
import com.stream.donalive.R;
import com.stream.donalive.streaming.internal.sdk.ZEGOSDKManager;
import com.stream.donalive.streaming.internal.sdk.components.OnRecyclerViewItemTouchListener;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyAbility;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyGroup;
import com.stream.donalive.streaming.internal.sdk.effect.bean.BeautyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeautyDialog extends BottomSheetDialog {

    private BeautyAdapter beautyAdapter;
    private SeekBarWithNumber seekBarWithNumber;
    private TabLayout tabLayout;
    private BeautyType currentSelectedType;

    public BeautyDialog(@NonNull Context context) {
        super(context, R.style.TransparentDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beauty_dialog_pop);

        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.1f;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        setCanceledOnTouchOutside(true);
        window.setBackgroundDrawable(new ColorDrawable());

        Map<BeautyGroup, String> beautyGroupTitles = new HashMap<>();
        beautyGroupTitles.put(BeautyGroup.BASIC, getContext().getString(R.string.beauty_group_basic));
        beautyGroupTitles.put(BeautyGroup.ADVANCED, getContext().getString(R.string.beauty_group_advanced));
        beautyGroupTitles.put(BeautyGroup.FILTERS, getContext().getString(R.string.beauty_group_filter));
        beautyGroupTitles.put(BeautyGroup.LIPSTICK, getContext().getString(R.string.beauty_group_makeup_lipstick));
        beautyGroupTitles.put(BeautyGroup.BLUSHER, getContext().getString(R.string.beauty_group_makeup_blusher));
        beautyGroupTitles.put(BeautyGroup.EYELASHES, getContext().getString(R.string.beauty_group_makeup_eyelashes));
        beautyGroupTitles.put(BeautyGroup.EYELINER, getContext().getString(R.string.beauty_group_makeup_eyeliner));
        beautyGroupTitles.put(BeautyGroup.EYESHADOW, getContext().getString(R.string.beauty_group_makeup_eyeshadow));
        beautyGroupTitles.put(BeautyGroup.COLORED_CONTACTS,
            getContext().getString(R.string.beauty_group_makeup_colored_contacts));
        beautyGroupTitles.put(BeautyGroup.STYLE_MAKEUP, getContext().getString(R.string.beauty_group_style_makeup));
        beautyGroupTitles.put(BeautyGroup.STICKERS, getContext().getString(R.string.beauty_group_sticker));
        beautyGroupTitles.put(BeautyGroup.BACKGROUND, getContext().getString(R.string.beauty_group_background));

        Map<BeautyGroup, List<BeautyItem>> beautyGroupItems = new HashMap<>();
        beautyGroupItems.put(BeautyGroup.BASIC, getBasicItems(getContext()));
        beautyGroupItems.put(BeautyGroup.ADVANCED, getAdvancedItems(getContext()));
        beautyGroupItems.put(BeautyGroup.FILTERS, getFilterItems(getContext()));
        beautyGroupItems.put(BeautyGroup.LIPSTICK, getMakeupLipstickItems(getContext()));
        beautyGroupItems.put(BeautyGroup.BLUSHER, getMakeupBlusherItems(getContext()));
        beautyGroupItems.put(BeautyGroup.EYELASHES, getMakeupEyelashesItems(getContext()));
        beautyGroupItems.put(BeautyGroup.EYELINER, getMakeupEyelinerItems(getContext()));
        beautyGroupItems.put(BeautyGroup.EYESHADOW, getMakeupEyeShadowItems(getContext()));
        beautyGroupItems.put(BeautyGroup.COLORED_CONTACTS, getMakeupColoredContactsItems(getContext()));
        beautyGroupItems.put(BeautyGroup.STYLE_MAKEUP, getMakeupStyleItems(getContext()));
        beautyGroupItems.put(BeautyGroup.STICKERS, getStickerItems(getContext()));
        beautyGroupItems.put(BeautyGroup.BACKGROUND, getBackgroundItems(getContext()));

        seekBarWithNumber = findViewById(R.id.seekBarWithNumber);
        seekBarWithNumber.setVisibility(View.INVISIBLE);
        seekBarWithNumber.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BeautyItem beautyItem = beautyAdapter.getBeautyItem(beautyAdapter.getSelectedItem());
                BeautyAbility beautyAbility = beautyItem.beautyAbility;
                if (beautyAbility != null) {
                    int progress = seekBar.getProgress();
                    if (beautyAbility.getBeautyTypes().size() == 1) {
                        BeautyType beautyType = beautyAbility.getBeautyTypes().get(0);
                        ZEGOSDKManager.getInstance().effectsService.setBeautyValue(beautyType, progress);
                    }
                }
            }
        });

        tabLayout = findViewById(R.id.beauty_tab_layout);
        assert tabLayout != null;
        tabLayout.setTabTextColors(Color.parseColor("#4dffffff"), Color.parseColor("#ffffffff"));

        List<BeautyGroup> beautyGroups = ZEGOSDKManager.getInstance().effectsService.getGroupConfigs();
        for (BeautyGroup value : beautyGroups) {
            Tab tab = tabLayout.newTab();
            BeautyTabInfo tabInfo = new BeautyTabInfo(value, beautyGroupTitles.get(value), beautyGroupItems.get(value));
            tab.setText(tabInfo.title);
            tab.setTag(tabInfo);
            tabLayout.addTab(tab);
        }

        tabLayout.getTabAt(0).select();
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                BeautyTabInfo tabInfo = (BeautyTabInfo) tab.getTag();
                beautyAdapter.setBeautyItems(tabInfo.beautyItems);
                beautyAdapter.setSelectedItem(tabInfo.selectedPosition);
                updateSeekBar();
            }

            @Override
            public void onTabUnselected(Tab tab) {
            }

            @Override
            public void onTabReselected(Tab tab) {
            }
        });

        RecyclerView recyclerviewItems = findViewById(R.id.recyclerview_items);
        beautyAdapter = new BeautyAdapter();
        Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
        BeautyTabInfo tabInfo = (BeautyTabInfo) selectedTab.getTag();
        beautyAdapter.setBeautyItems(tabInfo.beautyItems);
        recyclerviewItems.setAdapter(beautyAdapter);
        recyclerviewItems.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerviewItems.addOnItemTouchListener(new OnRecyclerViewItemTouchListener(recyclerviewItems) {
            @Override
            public void onItemClick(ViewHolder vh) {
                int position = vh.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                    BeautyTabInfo tabInfo = (BeautyTabInfo) selectedTab.getTag();
                    tabInfo.selectedPosition = position;
                    beautyAdapter.setSelectedItem(position);

                    BeautyItem beautyItem = beautyAdapter.getBeautyItem(position);
                    BeautyAbility beautyAbility = beautyItem.beautyAbility;
                    if (position == 0) {
                        seekBarWithNumber.setVisibility(View.INVISIBLE);
                        if (beautyAbility.getBeautyGroup() == BeautyGroup.BASIC) {
                            ZEGOSDKManager.getInstance().effectsService.resetBeautyBasics();
                        } else if (beautyAbility.getBeautyGroup() == BeautyGroup.ADVANCED) {
                            ZEGOSDKManager.getInstance().effectsService.resetBeautyAdvanced();
                        } else if (beautyAbility.getBeautyGroup() == BeautyGroup.BACKGROUND) {
                            ZEGOSDKManager.getInstance().effectsService.removeBackgrounds();
                        } else {
                            if (beautyAbility.getBeautyTypes().size() == 1) {
                                BeautyType beautyType = beautyAbility.getBeautyTypes().get(0);
                                ZEGOSDKManager.getInstance().effectsService.enableBeauty(beautyType, false);
                            }
                        }
                    } else {
                        if (beautyAbility.getBeautyGroup().isMakeup()
                            || beautyAbility.getBeautyGroup() == BeautyGroup.STICKERS) {
                            BeautyTabInfo styleMakeupInfo = getBeautyTabInfo(BeautyGroup.STYLE_MAKEUP);
                            if (styleMakeupInfo.selectedPosition != 0) {
                                styleMakeupInfo.selectedPosition = 0;
                                if (!styleMakeupInfo.beautyItems.isEmpty()) {
                                    BeautyAbility ability = styleMakeupInfo.beautyItems.get(0).beautyAbility;
                                    if (ability.getBeautyTypes().size() == 1) {
                                        BeautyType beautyType = ability.getBeautyTypes().get(0);
                                        ZEGOSDKManager.getInstance().effectsService.enableBeauty(beautyType, false);
                                    }
                                }
                            }
                        }
                        if (beautyAbility.getBeautyGroup() == BeautyGroup.STYLE_MAKEUP) {
                            for (BeautyGroup group : BeautyGroup.makeups) {
                                BeautyTabInfo info = getBeautyTabInfo(group);
                                info.selectedPosition = 0;
                                if (!info.beautyItems.isEmpty()) {
                                    BeautyAbility ability = info.beautyItems.get(0).beautyAbility;
                                    if (ability.getBeautyTypes().size() == 1) {
                                        BeautyType beautyType = ability.getBeautyTypes().get(0);
                                        ZEGOSDKManager.getInstance().effectsService.enableBeauty(beautyType, false);
                                    }
                                }
                            }
                            BeautyTabInfo stickerInfo = getBeautyTabInfo(BeautyGroup.STICKERS);
                            if (stickerInfo.selectedPosition != 0) {
                                stickerInfo.selectedPosition = 0;
                                if (!stickerInfo.beautyItems.isEmpty()) {
                                    BeautyAbility ability = stickerInfo.beautyItems.get(0).beautyAbility;
                                    if (ability.getBeautyTypes().size() == 1) {
                                        BeautyType beautyType = ability.getBeautyTypes().get(0);
                                        ZEGOSDKManager.getInstance().effectsService.enableBeauty(beautyType, false);
                                    }
                                }
                            }
                        }

                        //                        beautyAbility.getEditor().enable(true);
                        if (beautyAbility.getMaxValue() == beautyAbility.getMinValue()) {
                            seekBarWithNumber.setVisibility(View.INVISIBLE);
                        } else {
                            if (beautyAbility.getBeautyTypes().size() == 1) {
                                BeautyType beautyType = beautyAbility.getBeautyTypes().get(0);
                                ZEGOSDKManager.getInstance().effectsService.enableBeauty(beautyType, true);
                                seekBarWithNumber.setOffsetValue(beautyAbility.getMinValue());
                                seekBarWithNumber.setMax(beautyAbility.getMaxValue());
                                int value = ZEGOSDKManager.getInstance().effectsService.getBeautyValue(beautyType);
                                seekBarWithNumber.setProgress(value);
                                seekBarWithNumber.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                }
            }
        });
    }

    private void updateSeekBar() {
        int selectedItem = beautyAdapter.getSelectedItem();
        BeautyItem beautyItem = beautyAdapter.getBeautyItem(selectedItem);
        if (selectedItem == 0) {
            seekBarWithNumber.setVisibility(View.INVISIBLE);
        } else {
            seekBarWithNumber.setVisibility(View.VISIBLE);
            if (beautyItem.beautyAbility != null) {
                if (beautyItem.beautyAbility.getBeautyTypes().size() == 1) {
                    BeautyType beautyType = beautyItem.beautyAbility.getBeautyTypes().get(0);
                    int value = ZEGOSDKManager.getInstance().effectsService.getBeautyValue(beautyType);
                    seekBarWithNumber.setProgress(value);
                }
            }
        }
    }

    private BeautyTabInfo getBeautyTabInfo(BeautyGroup beautyGroup) {
        BeautyTabInfo result = null;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            BeautyTabInfo tabInfo = (BeautyTabInfo) tabLayout.getTabAt(i).getTag();
            if (tabInfo.beautyGroup == beautyGroup) {
                result = tabInfo;
                break;
            }
        }
        return result;
    }

    private List<BeautyItem> getBasicItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();
        BeautyItem reset = new BeautyItem(context.getString(R.string.beauty_reset),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_reset), BeautyType.BASIC_RESET);
        beautyItems.add(reset);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_basic_smoothing),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_smoothing), BeautyType.BASIC_SMOOTHING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_basic_skin_tone),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_skin_tone), BeautyType.BASIC_SKIN_TONE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_basic_blusher),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_blusher), BeautyType.BASIC_BLUSHER));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_basic_sharpening),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_sharpening), BeautyType.BASIC_SHARPENING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_basic_wrinkles),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_wrinkles), BeautyType.BASIC_WRINKLES));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_basic_dark_circles),
            ContextCompat.getDrawable(context, R.drawable.beauty_basic_dark_circles), BeautyType.BASIC_DARK_CIRCLES));
        return beautyItems;
    }

    private List<BeautyItem> getAdvancedItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem reset = new BeautyItem(context.getString(R.string.beauty_reset),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_reset), BeautyType.ADVANCED_RESET);
        beautyItems.add(reset);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_face_slimming),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_face_slimming),
            BeautyType.ADVANCED_FACE_SLIMMING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_eye_enlarging),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_eyes_enlarging),
            BeautyType.ADVANCED_EYES_ENLARGING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_eye_brightening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_eyes_brightening),
            BeautyType.ADVANCED_EYES_BRIGHTENING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_chin_lengthening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_chin_lengthening),
            BeautyType.ADVANCED_CHIN_LENGTHENING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_mouth_reshape),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_mouth_reshape),
            BeautyType.ADVANCED_MOUTH_RESHAPE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_teeth_whitening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_teeth_whitening),
            BeautyType.ADVANCED_TEETH_WHITENING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_nose_slimming),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_nose_slimming),
            BeautyType.ADVANCED_NOSE_SLIMMING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_nose_lengthening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_nose_lengthening),
            BeautyType.ADVANCED_NOSE_LENGTHENING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_face_shortening),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_face_shortening),
            BeautyType.ADVANCED_FACE_SHORTENING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_mandible_slimming),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_mandible_slimming),
            BeautyType.ADVANCED_MANDIBLE_SLIMMING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_cheekbone_slimming),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_cheekbone_slimming),
            BeautyType.ADVANCED_CHEEKBONE_SLIMMING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_advanced_forehead_slimming),
            ContextCompat.getDrawable(context, R.drawable.beauty_advanced_forehead_slimming),
            BeautyType.ADVANCED_FOREHEAD_SLIMMING));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupLipstickItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.MAKEUP_LIPSTICK_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_lipstick_cameo_pink),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_cameo_pink),
            BeautyType.MAKEUP_LIPSTICK_CAMEO_PINK));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_lipstick_sweet_orange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_sweet_orange),
            BeautyType.MAKEUP_LIPSTICK_SWEET_ORANGE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_lipstick_rust_red),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_rust_red),
            BeautyType.MAKEUP_LIPSTICK_RUST_RED));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_lipstick_coral),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_coral),
            BeautyType.MAKEUP_LIPSTICK_CORAL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_lipstick_red_velvet),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_lipstick_red_velvet),
            BeautyType.MAKEUP_LIPSTICK_RED_VELVET));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupBlusherItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.MAKEUP_BLUSHER_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_blusher_slightly_drunk),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_slightly_drunk),
            BeautyType.MAKEUP_BLUSHER_SLIGHTLY_DRUNK));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_blusher_peach),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_peach),
            BeautyType.MAKEUP_BLUSHER_PEACH));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_blusher_milky_orange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_milky_orange),
            BeautyType.MAKEUP_BLUSHER_MILKY_ORANGE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_blusher_milky_apricot_pink),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_apricot_pink),
            BeautyType.MAKEUP_BLUSHER_APRICOT_PINK));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_blusher_milky_sweet_orange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_blusher_sweet_orange),
            BeautyType.MAKEUP_BLUSHER_SWEET_ORANGE));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupEyelashesItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.MAKEUP_EYELASHES_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyelashes_natural),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_natural),
            BeautyType.MAKEUP_EYELASHES_NATURAL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyelashes_tender),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_tender),
            BeautyType.MAKEUP_EYELASHES_TENDER));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyelashes_curl),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_curl),
            BeautyType.MAKEUP_EYELASHES_CURL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyelashes_everlong),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_everlong),
            BeautyType.MAKEUP_EYELASHES_EVERLONG));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyelashes_thick),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyelashes_thick),
            BeautyType.MAKEUP_EYELASHES_THICK));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupEyelinerItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.MAKEUP_EYELINER_NONE);
        beautyItems.add(none);
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeliner_natural),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_natural),
            BeautyType.MAKEUP_EYELINER_NATURAL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeliner_cat_eye),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_cat_eye),
            BeautyType.MAKEUP_EYELINER_CAT_EYE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeliner_naughty),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_naughty),
            BeautyType.MAKEUP_EYELINER_NAUGHTY));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeliner_innocent),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_innocent),
            BeautyType.MAKEUP_EYELINER_INNOCENT));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeliner_dignified),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeliner_dignified),
            BeautyType.MAKEUP_EYELINER_DIGNIFIED));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupEyeShadowItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.MAKEUP_EYESHADOW_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeshadow_pink_mist),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_pink_mist),
            BeautyType.MAKEUP_EYESHADOW_PINK_MIST));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeshadow_shimmer_pink),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_shimmer_pink),
            BeautyType.MAKEUP_EYESHADOW_SHIMMER_PINK));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeshadow_tea_brown),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_tea_brown),
            BeautyType.MAKEUP_EYESHADOW_TEA_BROWN));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeshadow_bright_orange),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_bright_orange),
            BeautyType.MAKEUP_EYESHADOW_BRIGHT_ORANGE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_eyeshadow_mocha_brown),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_eyeshadow_mocha_brown),
            BeautyType.MAKEUP_EYESHADOW_MOCHA_BROWN));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupColoredContactsItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.MAKEUP_COLORED_CONTACTS_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_colored_contacts_darknight_black),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_darknight_black),
            BeautyType.MAKEUP_COLORED_CONTACTS_DARKNIGHT_BLACK));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_colored_contacts_starry_blue),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_starry_blue),
            BeautyType.MAKEUP_COLORED_CONTACTS_STARRY_BLUE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_colored_contacts_brown_green),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_brown_green),
            BeautyType.MAKEUP_COLORED_CONTACTS_BROWN_GREEN));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_colored_contacts_lights_brown),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_lights_brown),
            BeautyType.MAKEUP_COLORED_CONTACTS_LIGHTS_BROWN));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_makeup_colored_contacts_chocolate_contacts),
            ContextCompat.getDrawable(context, R.drawable.beauty_makeup_colored_contacts_chocolate_brown),
            BeautyType.MAKEUP_COLORED_CONTACTS_CHOCOLATE_BROWN));
        return beautyItems;
    }

    private List<BeautyItem> getMakeupStyleItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.STYLE_MAKEUP_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_group_style_innocent_eyes),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_innocent_eyes),
            BeautyType.STYLE_MAKEUP_INNOCENT_EYES));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_group_style_milky_eyes),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_milky_eyes),
            BeautyType.STYLE_MAKEUP_MILKY_EYES));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_group_style_cutie_cool),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_cutie_cool),
            BeautyType.STYLE_MAKEUP_CUTIE_COOL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_group_style_pure_sexy),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_pure_sexy),
            BeautyType.STYLE_MAKEUP_PURE_SEXY));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_group_style_flawless),
            ContextCompat.getDrawable(context, R.drawable.beauty_style_makeup_flawless),
            BeautyType.STYLE_MAKEUP_FLAWLESS));
        return beautyItems;
    }

    private List<BeautyItem> getFilterItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.FILTER_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_natural_creamy),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_creamy),
            BeautyType.FILTER_NATURAL_CREAMY));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_natural_brighten),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_brighten),
            BeautyType.FILTER_NATURAL_BRIGHTEN));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_natural_fresh),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_fresh),
            BeautyType.FILTER_NATURAL_FRESH));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_natural_autumn),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_natural_autumn),
            BeautyType.FILTER_NATURAL_AUTUMN));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_gray_monet),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_gray_monet), BeautyType.FILTER_GRAY_MONET));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_gray_night),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_gray_night), BeautyType.FILTER_GRAY_NIGHT));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_gray_filmlike),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_gray_filmlike),
            BeautyType.FILTER_GRAY_FILMLIKE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_dreamy_sunset),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_dreamy_sunset),
            BeautyType.FILTER_DREAMY_SUNSET));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_dreamy_cozily),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_dreamy_cozily),
            BeautyType.FILTER_DREAMY_COZILY));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_filter_dreamy_sweet),
            ContextCompat.getDrawable(context, R.drawable.beauty_filter_dreamy_sweet), BeautyType.FILTER_DREAMY_SWEET));
        return beautyItems;
    }

    private List<BeautyItem> getStickerItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.STICKER_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_animal),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_animal), BeautyType.STICKER_ANIMAL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_dive),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_dive), BeautyType.STICKER_DIVE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_cat),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_cat), BeautyType.STICKER_CAT));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_watermelon),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_watermelon), BeautyType.STICKER_WATERMELON));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_deer),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_deer), BeautyType.STICKER_DEER));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_cool_girl),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_cool_girl), BeautyType.STICKER_COOL_GIRL));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_clown),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_clown), BeautyType.STICKER_CLOWN));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_claw_machine),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_claw_machine),
            BeautyType.STICKER_CLAW_MACHINE));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_sticker_sailor_moon),
            ContextCompat.getDrawable(context, R.drawable.beauty_sticker_sailor_moon), BeautyType.STICKER_SAILOR_MOON));
        return beautyItems;
    }

    private List<BeautyItem> getBackgroundItems(Context context) {
        List<BeautyItem> beautyItems = new ArrayList<>();

        BeautyItem none = new BeautyItem(context.getString(R.string.beauty_none),
            ContextCompat.getDrawable(context, R.drawable.beauty_none), BeautyType.BACKGROUND_NONE);
        beautyItems.add(none);

        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_background_portrait_segmentation),
            ContextCompat.getDrawable(context, R.drawable.beauty_background_portrait_segmentation),
            BeautyType.BACKGROUND_PORTRAIT_SEGMENTATION));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_background_mosaicing),
            ContextCompat.getDrawable(context, R.drawable.beauty_background_mosaicing),
            BeautyType.BACKGROUND_MOSAICING));
        beautyItems.add(new BeautyItem(context.getString(R.string.beauty_background_gaussian_blur),
            ContextCompat.getDrawable(context, R.drawable.beauty_background_gaussian_blur),
            BeautyType.BACKGROUND_GAUSSIAN_BLUR));
        return beautyItems;
    }
}
