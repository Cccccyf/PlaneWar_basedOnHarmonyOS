package com.example.hit_the_plane.view;

import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.media.image.PixelMap;

public class Sprite {
    //屏幕宽高
    public int screenWidth;
    public int screenHeight;

    //承载图片的矩形
    public RectFloat rect = new RectFloat();

    //运动方向
    public int x_dir;
    public int y_dir;

    //宽高
    public int width;
    public int height;

    //移动速度
    public int speed;

    //可见性
    public boolean visibility = true;

    //bitmap
    public PixelMap pixelMap;
    public PixelMapHolder pixelMapHolder;

    //画笔
    public Paint paint;

    //生命
    public int life = 999;

    //当前帧数
    public int frame;

    public Sprite(PixelMap bitmap,int screenWidth,int screenHeight)
    {
        this.paint = new Paint();
        this.pixelMap = bitmap;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.pixelMapHolder = new PixelMapHolder(bitmap);
        this.width  = pixelMapHolder.getPixelMap().getImageInfo().size.width;
        this.height = pixelMapHolder.getPixelMap().getImageInfo().size.height;
        this.x_dir = Math.random()>0.5?1:-1;
        this.y_dir =1;
    }


    public void setOriginalRect(RectFloat rec)
    {
       this.rect = rec;
    }

    //目标坐标
    public void setDescRect(float offsetX,float offsetY)
    {
        if(rect.left + offsetX >= 0 && rect.right + offsetX <= screenWidth &&
                rect.top + offsetY >= 0 && rect.bottom + offsetY <= screenHeight) {
            rect.left = rect.left + offsetX;
            rect.right = rect.right + offsetX;
            rect.top = rect.top + offsetY;
            rect.bottom = rect.bottom + offsetY;
        }
    }

    public void onDraw(Component component,Canvas canvas,RectFloat rect,PixelMapHolder pixelHolder)
    {
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

    public void destroy()
    {
        this.pixelMap = null;
    }
}
