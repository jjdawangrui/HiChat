package com.itheima.hichat.view.activity;

/**
 * Created by Apple on 2016/12/3.
 */

public interface RegisterActivityView {
    void afterUploadUserIcon(boolean success);
    void afterSaveUserInfoToBmob(boolean success);
    void afterCreateEMAccount(boolean success);
    void afterLoginEMServer(boolean success);
}
