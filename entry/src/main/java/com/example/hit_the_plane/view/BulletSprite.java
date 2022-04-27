package com.example.hit_the_plane.view;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

import java.util.List;

public class BulletSprite extends Sprite{

    public BulletSprite(PixelMap bitmap, int screenWidth, int screenHeight,RectFloat rect,int dir,int speed) {
        super(bitmap, screenWidth, screenHeight);
        this.rect = rect;
        this.speed = speed;
        this.life = 0;
        this.y_dir = dir;
    }

    public void move(){
        this.rect.top += y_dir*speed;
        this.rect.bottom += y_dir*speed;
    }

    public void onDraw(Component component, Canvas canvas, PixelMapHolder pixelHolder){
//        for(Sprite s:sprites)
//        {
//            destroy(s);
//        }
        if(this.pixelMap!=null&&this.visibility){
            canvas.drawPixelMapHolderRect(pixelHolder,this.rect,paint);
            move();
        }
    }


    public void destroy(Sprite sprite){
        if(rect.bottom == screenHeight||rect.top == 0) {
            pixelMap = null;
            visibility = false;
        }
        if(sprite.pixelMap!=null) {
            if (((rect.top < sprite.rect.bottom && rect.bottom > sprite.rect.top && rect.right > sprite.rect.left
                    && rect.left < sprite.rect.right) || (sprite.rect.top < rect.bottom && sprite.rect.bottom > rect.top
                    && sprite.rect.right > rect.left && sprite.rect.left < rect.right))) {
                pixelMap = null;
                visibility = false;
            }
        }
    }
}
