package com.example.hit_the_plane.slice;

import com.example.hit_the_plane.ResourceTable;
import com.example.hit_the_plane.view.GameView;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.DirectionalLayout;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.Color;
import ohos.agp.utils.RectFloat;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GameAbilitySlice extends AbilitySlice {
    private DirectionalLayout layout = new DirectionalLayout(this);
    private RectFloat rectFloat = new RectFloat();
    private PixelMapHolder pixelmapholder;
    @Override
    public void onStart(Intent intent){
        super.onStart(intent);
        DirectionalLayout.LayoutConfig config = new DirectionalLayout.LayoutConfig(DirectionalLayout.LayoutConfig.MATCH_PARENT, DirectionalLayout.LayoutConfig.MATCH_PARENT);
        layout.setLayoutConfig(config);
        GameView gameView = new GameView(this);
        gameView.setLayoutConfig(config);
        layout.addComponent(gameView);
        setUIContent(layout);
    }

//    public void initialize(){
//        layout = new DirectionalLayout(this);
//        layout.setLayoutConfig((new ComponentContainer.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_PARENT,ComponentContainer.LayoutConfig.MATCH_PARENT)));
//        Component.DrawTask task=new Component.DrawTask() {
//            public void onDraw(Component component, Canvas canvas) {
//                rectFloat = new RectFloat(150,300,500,650);
//                Paint paint = new Paint();
//                paint.setColor(Color.RED);
//                PixelMap pixelmap = getPixelMap(ResourceTable.Media_bomb);
//                pixelmapholder = new PixelMapHolder(pixelmap);
//                canvas.drawPixelMapHolderRect(pixelmapholder,rectFloat, paint);
//            }
//        };
//        layout.addDrawTask(task);
//        layout.setTouchEventListener(mTouchEventListener);
//    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
