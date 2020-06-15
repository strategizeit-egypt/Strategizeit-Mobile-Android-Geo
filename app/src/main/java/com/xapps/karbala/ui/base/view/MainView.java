package com.xapps.karbala.ui.base.view;

public interface MainView {

    void onFinished();
    void onDismissLoader();
    void onShowLoader();
    void onTimeOut();
    void onError(int code);

}