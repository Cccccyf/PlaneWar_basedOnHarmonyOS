package com.example.hit_the_plane.slice;

import com.example.hit_the_plane.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;
import ohos.os.ProcessManager;

public class GameMainAbilitySlice extends AbilitySlice {
    private Button startGame;
    private Button rankList;
    private Button exit;
    @Override
    public void onStart(Intent intent){
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_game_main);
        initView();
        initListener();
    }

    public void initView(){
        startGame = (Button)findComponentById(ResourceTable.Id_start_game);
        rankList = (Button)findComponentById(ResourceTable.Id_rank_list);
        exit = (Button)findComponentById(ResourceTable.Id_exit);
    }

    public void initListener(){
        startGame.setClickedListener(listener->present(new GameAbilitySlice(),new Intent()));
        exit.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component){
                ProcessManager.kill(ProcessManager.getPid());
            }
        });//exit按钮点击直接退出进程
        rankList.setClickedListener(listener->present(new RankListAbilitySlice(),new Intent()));
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
