package com.example.hit_the_plane.slice;

import com.example.hit_the_plane.view.GameView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.DirectionalLayout;

import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.rdb.ValuesBucket;
import ohos.data.resultset.ResultSet;
import ohos.global.icu.text.SimpleDateFormat;


import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

import java.util.Date;

public class GameAbilitySlice extends AbilitySlice  implements Component.ClickedListener {
    //private static final HiLogLabel label = new HiLogLabel(3, 0xD001100, "Demo");

    public HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x00201,"TAG");
    private DataAbilityHelper databaseHelper;
    private static final String BASE_URI = "dataability:///com.example.hit_the_plane.DataAbility";
    private static final String DATA_PATH = "/record";
    private static final String DB_COLUMN_TIME = "time";
    private static final String DB_COLUMN_SCORE = "score";

    private final DirectionalLayout layout = new DirectionalLayout(this);
    private GameView gameView;
    @Override
    public void onStart(Intent intent){
        HiLog.info(label,"database start");
        super.onStart(intent);

        databaseHelper = DataAbilityHelper.creator(this);

        DirectionalLayout.LayoutConfig config = new DirectionalLayout.LayoutConfig(DirectionalLayout.LayoutConfig.MATCH_PARENT, DirectionalLayout.LayoutConfig.MATCH_PARENT);
        layout.setLayoutConfig(config);
        gameView= new GameView(this);
        gameView.setLayoutConfig(config);
        gameView.setClickedListener(this::onClick);
        layout.addComponent(gameView);
        setUIContent(layout);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void onClick(Component component) {
        if(gameView.planeSprite.life < -1 ){
            insert(gameView.score,getTime());
            HiLog.info(label,"database insert "+getTime()+" "+gameView.score);
            present(new GameMainAbilitySlice(),new Intent());
        }
    }

    /*
    获取时间
     */
    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss ");
        Date date = new Date();
        String str = sdf.format(date);
        return str;
    }

    //插入操作
    private void insert(int score, String time) {
        ValuesBucket valuesBucket = new ValuesBucket();
        valuesBucket.putString(DB_COLUMN_TIME, time);
        valuesBucket.putInteger(DB_COLUMN_SCORE, score);
        try {
            HiLog.info(label, "insert start");
            if (databaseHelper.insert(Uri.parse(BASE_URI + DATA_PATH), valuesBucket) != -1) {
                HiLog.info(label, "insert successful");
            }
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.info(label, "insert: dataRemote exception|illegalStateException");
        }
    }
}
