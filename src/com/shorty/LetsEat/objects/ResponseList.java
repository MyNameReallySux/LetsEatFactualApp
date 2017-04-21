package com.shorty.LetsEat.objects;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.shorty.LetsEat.utilities.KEYS;

import java.util.*;

public class ResponseList extends ArrayList<Map<String, Object>> implements Parcelable {
    public ResponseList(){
        super();
    }

    public ResponseList(Parcel in){
        Bundle bundle = in.readBundle();
        int size = in.readInt();

        Set<String> keySet = get(0).keySet();

        for(int i = 0; i <= size; i++){
            Map<String, Object> contents = new HashMap<String, Object>();
            for(String key : keySet){
                String value = getKeyInBundle(bundle, key, i);
                contents.put(key, value);
            }
            this.add(contents);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        int index = 0;
        for(Map<String, Object> contents : this){
            index++;
            for(String key : contents.keySet()){
                putKeyInBundle(bundle, contents, key, index);
            }
        }
        putIntInBundle(bundle, KEYS.SIZE, index);
        dest.writeBundle(bundle);
    }

    public String getKeyInBundle(Bundle bundle, String key, int index){
        return bundle.getString(key + index);
    }

    public void putKeyInBundle(Bundle bundle, Map<String, Object> contents, String key, int index){
        bundle.putString(key + index, contents.get(key).toString());
    }

    public void putKeySetInBundle(Bundle bundle, String key, Set<String> set){
        ArrayList<String> list = new ArrayList<String>(set);
        bundle.putStringArrayList(key, list);
    }

    public void putIntInBundle(Bundle bundle, String key, int size){
        bundle.putInt(key, size);
    }

    public static final Parcelable.Creator<ResponseList> CREATOR = new Parcelable.Creator<ResponseList>() {
        public ResponseList createFromParcel(Parcel in) {
            return new ResponseList(in);
        }

        public ResponseList[] newArray(int size) {
            return new ResponseList[size];
        }
    };
}
