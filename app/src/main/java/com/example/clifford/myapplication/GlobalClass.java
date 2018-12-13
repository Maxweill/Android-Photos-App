package com.example.clifford.myapplication;

import android.app.Application;

    public class GlobalClass extends Application{

        Account account;
        Album album;

        public Account getAccount() {
            return account;
        }

        public void setAccount(Account account) {
            this.account = account;
        }

        public Album getAlbum() {
            return album;
        }

        public void setAlbum(Album album) {
            this.album = album;
        }
    }
