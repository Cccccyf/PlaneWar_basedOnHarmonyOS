package com.example.hit_the_plane.view;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

public class PlaneSprite extends Sprite {

    public PlaneSprite(PixelMap bitmap, int screenWidth, int screenHeight) {
        super(bitmap, screenWidth, screenHeight);
        this.speed = 0;
        this.life = 30;
        this.y_dir = -1;
    }

    @Override
    public void onDraw(Component component, Canvas canvas, RectFloat rect, PixelMapHolder pixelHodler){
        frame++;
        //如果 bitmap 没有被销毁 并且是可见状态就绘制
        if(pixelMap !=null && visibility)
        {
            canvas.drawPixelMapHolderRect(pixelMapHolder,rect, paint);
        }
        //检查是否有交集
        if(this.life<=0)
        {
            destroy();
        }
    }
}
