package com.example.hit_the_plane.slice;


import com.example.hit_the_plane.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.ScrollView;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;


public class EnrolAbilitySlice extends AbilitySlice {
    private ScrollView view;
    private TextField phone_number;
    private TextField verify_code;
    private TextField passwordCreate;
    private Text valid_verify_code;
    @Override
    public void onStart(Intent intent){
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_enrol_page);
    }
    public void InitView(){

    }
}
