package com.example.hit_the_plane;

import com.example.hit_the_plane.slice.EnrolAbilitySlice;
import com.example.hit_the_plane.slice.GameAbilitySlice;
import com.example.hit_the_plane.slice.GameMainAbilitySlice;
import com.example.hit_the_plane.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(GameAbilitySlice.class.getName());
    }
}
