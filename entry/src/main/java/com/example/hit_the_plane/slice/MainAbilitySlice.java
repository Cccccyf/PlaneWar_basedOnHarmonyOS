package com.example.hit_the_plane.slice;

import com.example.hit_the_plane.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;

public class MainAbilitySlice extends AbilitySlice {
    private TextField userText;
    private TextField passwordText;
    private Text validUser;
    private Text validPassword;
    private Button enrolButton;
    private Button loginButton;
    private Button helpButton;  //找回密码
    private ScrollView loginScroll;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        initView();
        initListener();
    }

    /**
     * The initView, get component from xml
     */
    private void initView(){
        userText = (TextField)findComponentById(ResourceTable.Id_userText);
        passwordText = (TextField)findComponentById(ResourceTable.Id_passwordText);
        validUser = (Text)findComponentById(ResourceTable.Id_validUser);
        validPassword = (Text)findComponentById(ResourceTable.Id_validPassword);
        enrolButton = (Button)findComponentById(ResourceTable.Id_enrolButton);
        loginButton = (Button)findComponentById(ResourceTable.Id_loginButton);
        helpButton = (Button)findComponentById(ResourceTable.Id_helpButton);
        loginScroll = (ScrollView)findComponentById(ResourceTable.Id_loginScroll);
    }
    /**
     * The initListener, set listener of component
     */
    private void initListener() {
        userText.addTextObserver(
                (text, var, i1, i2) -> {
                    validUser.setVisibility(Component.HIDE);
                    validPassword.setVisibility(Component.HIDE);
                });
        passwordText.addTextObserver(this::onTextUpdated);
        loginButton.setClickedListener(component -> login(userText.getText(), passwordText.getText()));
        enrolButton.setClickedListener(listener -> present(new EnrolAbilitySlice(),new Intent()));
        helpButton.setClickedListener(listener -> present(new ResetAbilitySlice(),new Intent()));
    }
    /**
     * The onTextUpdated, change the loginButton background when password is empty or not
     * Hide the valid component when text change
     *
     * @param text text,the text of TextComponent
     * @param var  var
     * @param i1   i1
     * @param i2   i2
     */
    private void onTextUpdated(String text, int var, int i1, int i2) {
        if (text != null && !text.isEmpty()) {
            loginButton.setEnabled(true);
            loginButton.setBackground(new ShapeElement(this, ResourceTable.Graphic_background_login_can));
        } else {
            loginButton.setEnabled(false);
            loginButton.setBackground(new ShapeElement(this, ResourceTable.Graphic_background_login));
        }
        validUser.setVisibility(Component.HIDE);
        validPassword.setVisibility(Component.HIDE);
    }
    
    /**
     * The mailValid, valid the mail format local
     *
     * @param user mail,the text of mail
     * @return whether valid of the mail
     */
    private boolean userValid(String user) {
        return user.matches("^[a-z0-9A-Z]+[- |a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$");
    }// 应为查找数据库方法，需改写

    /**
     * The mailValid, valid the password local
     *
     * @param password password,the text of password
     * @return whether valid of the password
     */
    private boolean passwordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * The login, the login action when clicked loginButton
     * First, valid mail and password local
     * Second, show the login dialog
     * Third, a thread for valid the login info simulate local
     * Fourth, sendEvent whether login success or fail
     *
     * @param user     mail,the text of mail
     * @param password password,the text of password
     */
    private void login(final String user, final String password) {
        validUser.setVisibility(Component.HIDE);
        validPassword.setVisibility(Component.HIDE);

        if (!userValid(user)) {
            validUser.setVisibility(Component.VISIBLE);
        } else if (!passwordValid(password)) {
            validPassword.setVisibility(Component.VISIBLE);
        } else {
            present(new GameMainAbilitySlice(),new Intent());
        }
    }
    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
