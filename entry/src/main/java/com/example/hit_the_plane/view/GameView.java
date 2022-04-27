package com.example.hit_the_plane.view;

import com.example.hit_the_plane.ResourceTable;
import ohos.agp.components.Component;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.utils.RectFloat;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;

import java.io.IOException;
import java.util.*;


/*响应滑动事件或point_down事件，pointUp(不触摸屏幕即暂停游戏)*/
public class GameView extends Component implements Component.DrawTask, Component.TouchEventListener{
    //游戏状态(进行中/结束)
    public static final int STATUS_RUN = 1; //运行中
    public static final int STATUS_OVER = 250; //结束
    public int status;

    //为每一个sprite创建位图
    public PixelMap planePixel = getPixelMap(ResourceTable.Media_plane);
    public PixelMap smallPixel = getPixelMap(ResourceTable.Media_small);
    public PixelMap bigPixel = getPixelMap(ResourceTable.Media_big);
    public PixelMap blueBulletPixel = getPixelMap(ResourceTable.Media_blue_bullet);
    public PixelMap middlePixel = getPixelMap(ResourceTable.Media_middle);
    public PixelMap yellowBulletPixel = getPixelMap(ResourceTable.Media_yellow_bullet);
    public PixelMap bgPixel = getPixelMap(ResourceTable.Media_bg);
    public PixelMap bombPixel = getPixelMap(ResourceTable.Media_bomb);
    public PixelMap bossPixel = getPixelMap(ResourceTable.Media_cr);
    public PixelMap explosionPixel = getPixelMap(ResourceTable.Media_explosion);

    //pixelMapHolder(未创建类的)
    private final PixelMapHolder bgHolder = new PixelMapHolder(bgPixel);
    private final PixelMapHolder explosion = new PixelMapHolder(explosionPixel);

    //屏幕宽高
    public int screenWidth;
    public int screenHeight;

    //绘制背景的矩形
    private RectFloat recBg;


    //储存己方战机和Boss(?)
    public List<Sprite> sprites = new ArrayList<Sprite>();

    //创建己方战机子弹
    public List<BulletSprite> planeBullets = new ArrayList<>();

    //储存敌机链表、包括大中小三号
    public List<SmallSprite> smallPlanes = new ArrayList<>();
    public List<MiddleSprite> middlePlanes = new ArrayList<>();
    public List<BigSprite> bigPlanes = new ArrayList<>();

    //储存敌机子弹
    public List<BulletSprite> enemyBullets = new ArrayList<>();

    //唯一boss
    public BossSprite boss;

    //设置手指触摸的点
    public int x, y;
    public MmiPoint point;

    //用于计算(帧数)，判断是否进行某种行为
    public int frame = 0;

    //得分
    public int score = 0;

    //画笔
    public Paint paint = new Paint();
    public Paint textPaint = new Paint();

    //主要(唯一)战机
    public PlaneSprite planeSprite = new PlaneSprite(planePixel, screenWidth, screenHeight);

    public GameView(Context context) {
        super(context);
        //初始化游戏运行状态
        this.status = STATUS_RUN;
        //初始化屏幕长宽
        screenWidth = getScreenWidth(context);
        screenHeight = getScreenHeight(context);
        recBg = new RectFloat(0, 0, screenWidth, screenHeight);
        //boss初始化
        boss= new BossSprite(bossPixel, screenWidth, screenHeight);
        //画笔
        textPaint.setTextSize(50);

        //初始化战机和Boss
        //初始化boss和战机位置
        planeSprite.rect = new RectFloat(screenWidth / 2 - 100, screenHeight - 400, screenWidth / 2 + 100, screenHeight - 200);
        addDrawTask(this::onDraw);

        //设置监听
        setTouchEventListener(this::onTouchEvent);
    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
            //生成敌方战机
            if (frame % 100 == 1) {
                if(frame%500==1&&frame!=1){
                    addBigSprite();
                }else if(frame%300==1){
                    addMiddleSprite();
                }else {
                    addSmallSprite();
                }
            }

            //己方战机发射子弹
            if (frame % 10 == 0) {
                shoot(planeSprite, planeBullets, blueBulletPixel, 50);
            }

            //敌方战机发射子弹
            if (frame % 50 == 0) {
                for (SmallSprite each : smallPlanes) {
                    if (each.visibility) {
                        shoot(each, enemyBullets, yellowBulletPixel, 20);
                    }
                }
                for (MiddleSprite each : middlePlanes) {
                    if (each.visibility) {
                        shoot(each, enemyBullets, yellowBulletPixel, 20);
                    }
                }
                for (BigSprite each : bigPlanes) {
                    if (each.visibility) {
                        shoot(each, enemyBullets, yellowBulletPixel, 20);
                    }
                }
            }

            //frame++
            this.frame += 1;



            //绘制背景
            canvas.drawPixelMapHolderRect(bgHolder, recBg, paint);

            //判断己方子弹是否击中敌机
            for (SmallSprite sprite : smallPlanes) {
                if(sprite.pixelMap!=null) {
                    for (BulletSprite blue : planeBullets) {
                        if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                            blue.pixelMap = null;
                            sprite.life--;
                        }
                    }
                }
            }
            for (MiddleSprite sprite : middlePlanes) {
                if(sprite.pixelMap!=null) {
                    for (BulletSprite blue : planeBullets) {
                        if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                            blue.pixelMap = null;
                            sprite.life--;
                        }
                    }
                }
            }
            for (BigSprite sprite : bigPlanes) {
                if(sprite.pixelMap!=null) {
                    for (BulletSprite blue : planeBullets) {
                        if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                            blue.pixelMap = null;
                            sprite.life--;
                        }
                    }
                }
            }
            for(BulletSprite blue:planeBullets) {
                if (boss.pixelMap != null&&isIntersect(blue,boss)){
                    blue.pixelMap = null;
                    boss.life--;
                }
            }

            //判断己方是否被击中
            for (BulletSprite yellow : enemyBullets) {
                if (yellow.pixelMap != null && isIntersect(yellow, planeSprite)) {
                    yellow.pixelMap = null;
                    planeSprite.life--;
                }
            }

            //己方与Boss的绘制
            planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
            boss.onDraw(component,canvas,boss.rect,boss.pixelMapHolder);

            //己方子弹的绘制
            for (BulletSprite each : planeBullets) {
                each.onDraw(component, canvas, each.pixelMapHolder, sprites);
            }

            //敌机(小兵)的绘制
           drawEnemy(component,canvas);

            //敌方子弹的绘制
            for (BulletSprite each : enemyBullets) {
                each.onDraw(component, canvas, each.pixelMapHolder, sprites);
            }
            drawScore(canvas);
            drawLife(canvas);
    }

    private void Init() {

    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        if(planeSprite.life>=0) {
            //生成敌方战机
            if (frame % 100 == 1) {
                if (frame % 500 == 1 && frame != 1) {
                    addBigSprite();
                } else if (frame % 300 == 1) {
                    addMiddleSprite();
                } else {
                    addSmallSprite();
                }
            }

            //己方战机发射子弹
            if (frame % 10 == 0) {
                shoot(planeSprite, planeBullets, blueBulletPixel, 50);
            }

            //敌方战机发射子弹
            if (frame % 50 == 0) {
                for (SmallSprite each : smallPlanes) {
                    if (each.visibility) {
                        shoot(each, enemyBullets, yellowBulletPixel, 20);
                    }
                }
                for (MiddleSprite each : middlePlanes) {
                    if (each.visibility) {
                        shoot(each, enemyBullets, yellowBulletPixel, 20);
                    }
                }
                for (BigSprite each : bigPlanes) {
                    if (each.visibility) {
                        shoot(each, enemyBullets, yellowBulletPixel, 20);
                    }
                }
            }

            //frame++
            this.frame += 1;
            switch (touchEvent.getAction()) {
                case TouchEvent.PRIMARY_POINT_DOWN:
                    point = touchEvent.getPointerPosition(touchEvent.getIndex());
                    x = (int) point.getX();
                    y = (int) point.getY();
                    DrawTask nDrawTask = new DrawTask() {
                        public void onDraw(Component component, Canvas canvas) {
                            canvas.drawPixelMapHolderRect(bgHolder, recBg, paint);
                            if (planeSprite.life > 0) {
                                //判断敌方被击中
                                hitEnemy();

                                //判断己方被击中
                                for (BulletSprite yellow : enemyBullets) {
                                    if (yellow.pixelMap != null && isIntersect(yellow, planeSprite)) {
                                        yellow.pixelMap = null;
                                        planeSprite.life--;
                                    }
                                }

                                planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
                                boss.onDraw(component, canvas, boss.rect, boss.pixelMapHolder);

                                //己方子弹的绘制
                                for (BulletSprite each : planeBullets) {
                                    each.onDraw(component, canvas, each.pixelMapHolder, sprites);
                                }

                                //敌机(小兵)的绘制
                                drawEnemy(component, canvas);

                                //敌方子弹的绘制
                                for (BulletSprite each : enemyBullets) {
                                    each.onDraw(component, canvas, each.pixelMapHolder, sprites);
                                }
                                drawScore(canvas);
                                drawLife(canvas);
                            } else {
                                planeSprite.setOriginalRect(new RectFloat(0, planeSprite.rect.top, screenWidth, planeSprite.rect.bottom));
                                planeSprite.pixelMapHolder = explosion;
                                planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
                                boss.onDraw(component, canvas, boss.rect, boss.pixelMapHolder);
                                drawEnemy(component, canvas);
                                drawScore(canvas);
                                drawLife(canvas);
                            }
                        }
                    };
                    addDrawTask(nDrawTask);

                    return true;
                case TouchEvent.POINT_MOVE:
                    MmiPoint new_point = touchEvent.getPointerPosition(touchEvent.getIndex());
                    int deltaX = (int) new_point.getX() - x;
                    int deltaY = (int) new_point.getY() - y;
                    x = (int) new_point.getX();
                    y = (int) new_point.getY();
                    if (planeSprite.rect.left + deltaX >= 0 && planeSprite.rect.right + deltaX <= screenWidth && planeSprite.rect.top + deltaY >= 0 && planeSprite.rect.bottom + deltaY <= screenHeight) {
                        planeSprite.rect.left = planeSprite.rect.left + deltaX;
                        planeSprite.rect.right = planeSprite.rect.right + deltaX;
                        planeSprite.rect.top = planeSprite.rect.top + deltaY;
                        planeSprite.rect.bottom = planeSprite.rect.bottom + deltaY;
                    }

                    //判断碰撞事件
                    if (isIntersect(planeSprite, boss)) {
                        planeSprite.life--;
                        boss.life--;
                    }
                    for (SmallSprite sprite : smallPlanes) {
                        if (sprite.pixelMap != null && isIntersect(sprite, planeSprite)) {
                            sprite.life--;
                            planeSprite.life--;
                        }
                    }
                    for (MiddleSprite sprite : middlePlanes) {
                        if (sprite.pixelMap != null && isIntersect(sprite, planeSprite)) {
                            sprite.life--;
                            planeSprite.life--;
                        }
                    }
                    for (BigSprite sprite : bigPlanes) {
                        if (sprite.pixelMap != null && isIntersect(sprite, planeSprite)) {
                            sprite.life--;
                            planeSprite.life--;
                        }
                    }

                    DrawTask mDrawTask = new DrawTask() {
                        public void onDraw(Component component, Canvas canvas) {
                            canvas.drawPixelMapHolderRect(bgHolder, recBg, paint);
                            if (planeSprite.life > 0) {
                                //敌方被击中
                                hitEnemy();

                                //己方被击中
                                for (BulletSprite yellow : enemyBullets) {
                                    if (yellow.pixelMap != null && isIntersect(yellow, planeSprite)) {
                                        yellow.pixelMap = null;
                                        planeSprite.life--;
                                    }
                                }

                                planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
                                boss.onDraw(component, canvas, boss.rect, boss.pixelMapHolder);

                                //己方子弹的绘制
                                for (BulletSprite each : planeBullets) {
                                    each.onDraw(component, canvas, each.pixelMapHolder, sprites);
                                }

                                //敌机(小兵)的绘制
                                drawEnemy(component, canvas);

                                //敌方子弹的绘制
                                for (BulletSprite each : enemyBullets) {
                                    each.onDraw(component, canvas, each.pixelMapHolder, sprites);
                                }
                                drawScore(canvas);
                                drawLife(canvas);
                            } else {
                                planeSprite.setOriginalRect(new RectFloat(0, planeSprite.rect.top, screenWidth, planeSprite.rect.bottom));
                                planeSprite.pixelMapHolder = explosion;
                                planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
                                boss.onDraw(component, canvas, boss.rect, boss.pixelMapHolder);
                                drawEnemy(component, canvas);
                                drawScore(canvas);
                                drawLife(canvas);
                            }
                        }
                    };
                    addDrawTask(mDrawTask);
                    return true;
                default:
                    DrawTask yDrawTask = new DrawTask() {
                        public void onDraw(Component component, Canvas canvas) {
                            canvas.drawPixelMapHolderRect(bgHolder, recBg, paint);
                            if (planeSprite.life > 0) {
                                //敌方被击中
                                for (SmallSprite sprite : smallPlanes) {
                                    if (sprite.pixelMap != null) {
                                        for (BulletSprite blue : planeBullets) {
                                            if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                                                blue.pixelMap = null;
                                                sprite.life--;
                                            }
                                        }
                                    }
                                }
                                for (MiddleSprite sprite : middlePlanes) {
                                    if (sprite.pixelMap != null) {
                                        for (BulletSprite blue : planeBullets) {
                                            if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                                                blue.pixelMap = null;
                                                sprite.life--;
                                            }
                                        }
                                    }
                                }
                                for (BigSprite sprite : bigPlanes) {
                                    if (sprite.pixelMap != null) {
                                        for (BulletSprite blue : planeBullets) {
                                            if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                                                blue.pixelMap = null;
                                                sprite.life--;
                                            }
                                        }
                                    }
                                }
                                for (BulletSprite blue : planeBullets) {
                                    if (blue.pixelMap != null) {
                                        if (boss.pixelMap != null && isIntersect(blue, boss)) {
                                            blue.pixelMap = null;
                                            score += 1;
                                            boss.life--;
                                        }
                                    }
                                }
                                //己方被击中
                                for (BulletSprite yellow : enemyBullets) {
                                    if (yellow.pixelMap != null && isIntersect(yellow, planeSprite)) {
                                        yellow.pixelMap = null;
                                        planeSprite.life--;
                                    }
                                }

                                planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
                                boss.onDraw(component, canvas, boss.rect, boss.pixelMapHolder);

                                //己方子弹的绘制
                                for (BulletSprite each : planeBullets) {
                                    each.onDraw(component, canvas, each.pixelMapHolder, sprites);
                                }

                                //敌机(小兵)的绘制
                                drawEnemy(component, canvas);

                                //敌方子弹的绘制
                                for (BulletSprite each : enemyBullets) {
                                    each.onDraw(component, canvas, each.pixelMapHolder, sprites);
                                }
                                drawScore(canvas);
                                drawLife(canvas);
                            } else if(planeSprite.life==0){
                                planeSprite.setOriginalRect(new RectFloat(0, planeSprite.rect.top, screenWidth, planeSprite.rect.bottom));
                                planeSprite.pixelMapHolder = explosion;
                                planeSprite.onDraw(component, canvas, planeSprite.rect, planeSprite.pixelMapHolder);
                                boss.onDraw(component, canvas, boss.rect, boss.pixelMapHolder);
                                drawEnemy(component, canvas);
                                drawScore(canvas);
                                drawLife(canvas);
                            }
                        }
                    };
                    addDrawTask(yDrawTask);
                    return true;
            }
        }else {
            if (touchEvent.getAction() == TouchEvent.PRIMARY_POINT_DOWN) {
                DrawTask end = new DrawTask() {
                    @Override
                    public void onDraw(Component component, Canvas canvas) {
                        canvas.drawPixelMapHolderRect(bgHolder, recBg, paint);
                        canvas.drawPixelMapHolderRect(boss.pixelMapHolder, boss.rect, paint);

                        for (SmallSprite sprite : smallPlanes) {
                            if (sprite.pixelMap != null) {
                                canvas.drawPixelMapHolderRect(sprite.pixelMapHolder, sprite.rect, paint);
                            }
                        }
                        for (MiddleSprite sprite : middlePlanes) {
                            if (sprite.pixelMap != null) {
                                canvas.drawPixelMapHolderRect(sprite.pixelMapHolder, sprite.rect, paint);
                            }
                        }
                        for (BigSprite sprite : bigPlanes) {
                            if (sprite.pixelMap != null) {
                                canvas.drawPixelMapHolderRect(sprite.pixelMapHolder, sprite.rect, paint);
                            }
                        }
                    }
                };
                addDrawTask(end);
                return true;
            }
            return false;
        }
    }

    public void drawScore(Canvas canvas){
        String text = "分数：" + this.score;
        canvas.drawText(textPaint,text,100,100);
    }
    public void drawLife(Canvas canvas){
        String text = "生命值：" + planeSprite.life;
        canvas.drawText(textPaint,text,100,screenHeight-300);
    }


    //添加敌方战机
    public void addSmallSprite () {
        SmallSprite enemy = new SmallSprite(smallPixel, screenWidth, screenHeight);
        smallPlanes.add(enemy);
    }

    public void addMiddleSprite(){
        MiddleSprite enemy = new MiddleSprite(middlePixel,screenWidth,screenHeight);
        middlePlanes.add(enemy);
    }

    public void addBigSprite(){
        BigSprite enemy = new BigSprite(bigPixel,screenWidth,screenHeight);
        bigPlanes.add(enemy);
    }

    //发射子弹
    public void shoot(Sprite sprite,List<BulletSprite> bullets,PixelMap pixel,int speed){
        RectFloat rectBullet = new RectFloat((sprite.rect.left+sprite.rect.right)/2-15,sprite.rect.top-25,
                (sprite.rect.left+sprite.rect.right)/2+15,sprite.rect.top+25);
        BulletSprite bullet = new BulletSprite(pixel,screenWidth,screenHeight,rectBullet,sprite.y_dir,speed);
        bullets.add(bullet);
    }

    //绘制敌方战机
    public void drawEnemy(Component component,Canvas canvas){
        for (SmallSprite each : smallPlanes) {
            if(each.visibility){
                score = each.onDraw(component, canvas, each.rect, each.pixelMapHolder, score);
            }
        }
        for (MiddleSprite each : middlePlanes) {
            if(each.visibility){
                score = each.onDraw(component, canvas, each.rect, each.pixelMapHolder, score);
            }
        }
        for (BigSprite each : bigPlanes) {
            if(each.visibility){
                score = each.onDraw(component, canvas, each.rect, each.pixelMapHolder, score);
            }
        }
    }

    //子弹击中敌方
    public void hitEnemy(){
        for (SmallSprite sprite : smallPlanes) {
            if(sprite.pixelMap!=null) {
                for (BulletSprite blue : planeBullets) {
                    if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                        blue.pixelMap = null;
                        sprite.life--;
                    }
                }
            }
        }
        for (MiddleSprite sprite : middlePlanes) {
            if(sprite.pixelMap!=null) {
                for (BulletSprite blue : planeBullets) {
                    if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                        blue.pixelMap = null;
                        sprite.life--;
                    }
                }
            }
        }
        for (BigSprite sprite : bigPlanes) {
            if(sprite.pixelMap!=null) {
                for (BulletSprite blue : planeBullets) {
                    if (blue.pixelMap != null && isIntersect(sprite, blue)) {
                        blue.pixelMap = null;
                        sprite.life--;
                    }
                }
            }
        }
        for(BulletSprite blue:planeBullets) {
            if(blue.pixelMap!=null) {
                if (boss.pixelMap != null && isIntersect(blue, boss)) {
                    blue.pixelMap = null;
                    score += 1;
                    boss.life--;
                }
            }
        }
    }
    /**
     *判断碰撞事件是否发生
     **/
    public boolean isIntersect (Sprite sprite1, Sprite sprite2){
        if ((sprite1.rect.top < sprite2.rect.bottom && sprite1.rect.bottom > sprite2.rect.top && sprite1.rect.right > sprite2.rect.left
                && sprite1.rect.left < sprite2.rect.right) || (sprite2.rect.top < sprite1.rect.bottom && sprite2.rect.bottom > sprite1.rect.top
                && sprite2.rect.right > sprite1.rect.left && sprite2.rect.left < sprite1.rect.right)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取屏幕宽
     * @param context c
     * @return int
     */
    public static int getScreenWidth (Context context){
        return context.getResourceManager().getDeviceCapability().width
                * context.getResourceManager().getDeviceCapability().screenDensity
                / 160;
    }

    /**
     * 获取屏幕高
     * @param context c
     * @return int
     */
    public static int getScreenHeight (Context context){
        return context.getResourceManager().getDeviceCapability().height
                * context.getResourceManager().getDeviceCapability().screenDensity
                / 160;
    }

    //根据resId获取位图
    public PixelMap getPixelMap ( int resId){
        Resource bgResource = null;
        try {
            bgResource = getResourceManager().getResource(resId);
        } catch (IOException | NotExistException e) {
            e.printStackTrace();
        }
        ImageSource.SourceOptions sourceOptions = new ImageSource.SourceOptions();
        sourceOptions.formatHint = "image/png";
        ImageSource imageSource = ImageSource.create(bgResource, sourceOptions);
        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
        decodingOptions.desiredSize = new Size(200, 200);//设置图像尺寸
        decodingOptions.desiredRegion = new Rect(0, 0, 0, 0); //不进行裁剪
        decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;//设置位图格式
        return imageSource.createPixelmap(decodingOptions);
    }
}