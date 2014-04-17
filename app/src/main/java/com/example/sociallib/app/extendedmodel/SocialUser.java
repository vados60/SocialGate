package com.example.sociallib.app.extendedmodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class SocialUser implements Parcelable{
    private String name;
    private String surname;
    private String email;

    public SocialUser(){}

    public SocialUser(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
      public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeStringArray(new String[] { name, surname, email });
    }

    public static final Parcelable.Creator<SocialUser> CREATOR = new Parcelable.Creator<SocialUser>() {
        // распаковываем объект из Parcel
        public SocialUser createFromParcel(Parcel in) {
            Log.d("SocialUser", "createFromParcel");
            return new SocialUser(in);
        }

        public SocialUser[] newArray(int size) {
            return new SocialUser[size];
        }
    };

    private SocialUser(Parcel parcel) {
        Log.d("SocialUser", "MyObject(Parcel parcel)");
        String[] data = new String[3];
        parcel.readStringArray(data);
        name = data[0];
        surname = data[1];
        email = data[2];

    }
}


