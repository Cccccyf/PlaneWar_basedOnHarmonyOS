package com.example.hit_the_plane.view;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

public class BossSprite extends Sprite {
    public BossSprite(PixelMap bitmap, int screenWidth, int screenHeight) {
        super(bitmap, screenWidth, screenHeight);
        this.rect.left = 300;
        this.rect.right = 600;
        this.rect.top = 0;
        this.rect.bottom = 300;
        this.speed = 3;
        this.life = 99999;
        //this.x_dir = Math.random()>0.5?-1:1;
        this.x_dir = -1;
        this.y_dir = 0;
    }

    //Boss的移动
    public void move(){
        if((rect.left + x_dir*speed >= 0)&&(rect.right + x_dir*speed <= screenWidth)){
            rect.left += x_dir*this.speed;
            rect.right += x_dir*this.speed;
        }else if(rect.left + x_dir*speed < 0){
            rect.left = 0;
            rect.right = 300;
            x_dir *= -1;
        }else if(rect.right + x_dir*speed > screenWidth){
            rect.left = screenWidth-300;
            rect.right = screenWidth;
            x_dir *= -1;
        }
    }

    public void onDraw(Component component, Canvas canvas, RectFloat rect, PixelMapHolder pixelHolder){
        move();
        //如果 bitmap 没有被销毁 并且是可见状态就绘制
        if(pixelMap !=null && visibility)
        {
            canvas.drawPixelMapHolderRect(pixelMapHolder,rect, paint);

        }
        if(this.life == 0)
        {
            destroy();
        }
    }
}
