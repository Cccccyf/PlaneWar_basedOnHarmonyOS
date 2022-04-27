package com.example.hit_the_plane.view;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

public class BigSprite extends Sprite{
    public int my_score = 75;
    public BigSprite(PixelMap bitmap, int screenWidth, int screenHeight) {
        super(bitmap, screenWidth, screenHeight);
        float ori = (float) Math.random();
        this.rect.left = (float) ((screenWidth - 200) * ori);
        this.rect.right = (float) (this.rect.left + 250);
        this.rect.top = (float) ((screenHeight-800)*ori);
        this.rect.bottom = (float)(this.rect.top+250);
        this.speed = 1;
        this.life = 10;
        this.y_dir = 1;
    }
    public void move(){
        if((rect.left + speed*x_dir >= 0)&&(rect.right + speed*x_dir <= screenWidth)){
            rect.left = rect.left + speed*x_dir;
            rect.right = rect.right + speed*x_dir;
        }else if(rect.right + speed*x_dir > screenWidth){
            rect.left = screenWidth-250;
            rect.right = screenWidth;
            x_dir = -1 * x_dir;
        }else if(rect.left + speed*x_dir<0){
            rect.left = 0;
            rect.right = 250;
            x_dir = -1*x_dir;
        }
        rect.top += speed;
        rect.bottom += speed;
    }

    public int onDraw(Component component, Canvas canvas, RectFloat rect, PixelMapHolder pixelHolder, int score) {
        move();
        if(this.pixelMap == null) return score;
        if(rect.bottom>screenHeight){
            this.visibility = false;
            destroy();
        }else if(this.life<=0){
            score += my_score;
            this.visibility = false;
            destroy();
        }
        if(pixelMap !=null && visibility)
        {
            canvas.drawPixelMapHolderRect(pixelMapHolder,rect, paint);
        }
        return score;
    }
}
