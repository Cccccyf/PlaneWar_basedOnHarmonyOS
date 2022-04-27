package com.example.hit_the_plane.slice;

import com.example.hit_the_plane.DataAbility;
import com.example.hit_the_plane.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.ability.DataAbilityHelper;
import ohos.aafwk.ability.DataAbilityRemoteException;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.utils.LayoutAlignment;
import ohos.data.DatabaseHelper;
import ohos.data.dataability.DataAbilityPredicates;
import ohos.data.resultset.ResultSet;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.Image;
import ohos.media.image.PixelMap;
import ohos.media.photokit.metadata.AVStorage;
import ohos.utils.net.Uri;

import java.util.*;
import com.example.hit_the_plane.view.GameView;
public class RankListAbilitySlice extends AbilitySlice {
    //储存分数和时间的键值对
    public Map<Integer,String>  record = new HashMap<>();
    //储存分数的链表，便于排序
    public List<Integer> scores = new ArrayList<>();
    
    public HiLogLabel label = new HiLogLabel(HiLog.LOG_APP,0x00201,"TAG");
    
    private DataAbilityHelper databaseHelper;
    private static final String BASE_URI = "dataability:///com.example.hit_the_plane.DataAbility";
    private static final String DATA_PATH = "/record";
    private static final String DB_COLUMN_TIME = "time";
    private static final String DB_COLUMN_SCORE = "score";

    public Button button = new Button(getContext());
    public Text text0 = new Text(getContext());
    public Text text1 = new Text(getContext());
    public Text text2 = new Text(getContext());
    public Text text3 = new Text(getContext());
    public Text text4 = new Text(getContext());
    public Text text5 = new Text(getContext());
    public Text text6 = new Text(getContext());
    public Text text7 = new Text(getContext());
    public Text text8 = new Text(getContext());
    public Text text9 = new Text(getContext());

    private DirectionalLayout.LayoutConfig layoutConfig;
    private final DirectionalLayout layout = new DirectionalLayout(this);
    @Override
    public void onStart(Intent intent){
        super.onStart(intent);
        //数据库初始化
        databaseHelper = DataAbilityHelper.creator(this);

        layout.setOrientation(Component.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setWidth(ComponentContainer.LayoutConfig.MATCH_PARENT);
        layout.setHeight(ComponentContainer.LayoutConfig.MATCH_PARENT);


        layoutConfig= new DirectionalLayout.LayoutConfig(ComponentContainer.LayoutConfig.MATCH_CONTENT, ComponentContainer.LayoutConfig.MATCH_CONTENT);
        layoutConfig.alignment = LayoutAlignment.HORIZONTAL_CENTER;

        initialize();
        //按钮点击事件设置
        button.setClickedListener(Listener->present(new GameMainAbilitySlice(),new Intent()));
        setUIContent(layout);
    }
    
    public void initialize(){
        sort();
        Text[] t = new Text[10];
        t[0] = text0;
        t[1] = text1;
        t[2] = text2;
        t[3] = text3;
        t[4] = text4;
        t[5] = text5;
        t[6] = text6;
        t[7] = text7;
        t[8] = text8;
        t[9] = text9;
        button.setText("返回");
        button.setTextSize(100);
        button.setPadding(32, 32, 32, 32);
        button.setLayoutConfig(layoutConfig);
        layout.addComponent(button);

        for(int i = 0;i<10;i++){
            if(i<scores.size()){
                t[i].setText("时间："+ record.get(scores.get(i))+" 分数："+scores.get(i));
            }else{
                t[i].setText("NO DATA");
            }
            t[i].setPadding(0, 40, 0, 40);
            t[i].setTextSize(50);
            t[i].setWidth(ComponentContainer.LayoutConfig.MATCH_CONTENT);
            t[i].setHeight(ComponentContainer.LayoutConfig.MATCH_CONTENT);
            t[i].setLayoutConfig(layoutConfig);
            layout.addComponent(t[i]);
        }
    }

    /*
    处理记录
     */
    public void sort(){
        query();
        Collections.sort(scores);
        Collections.reverse(scores);
    }

    /*
    取数据
     */
    private void query() {
        String[] columns = new String[] {DB_COLUMN_TIME,DB_COLUMN_SCORE};
        // 构造查询条件
        DataAbilityPredicates predicates = new DataAbilityPredicates();
        predicates.between("score",0,9999999);// 查询分数段
        try {
            ResultSet resultSet = databaseHelper.query(Uri.parse(BASE_URI + DATA_PATH),
                    columns, predicates);
            if (resultSet == null || resultSet.getRowCount() == 0) {
                HiLog.info(label, "query: resultSet is null or no result found");
                return;
            }
            resultSet.goToFirstRow();
            do {
                int score = resultSet.getInt(resultSet.getColumnIndexForName(DB_COLUMN_SCORE));
                String time = resultSet.getString(resultSet.getColumnIndexForName(DB_COLUMN_TIME));
                scores.add(score);
                record.put(score,time);
                HiLog.info(label, "query: time :" + time + " score :" + score);
            } while (resultSet.goToNextRow());
        } catch (DataAbilityRemoteException | IllegalStateException exception) {
            HiLog.info(label, "query: dataRemote exception | illegalStateException");
        }
    }
}
