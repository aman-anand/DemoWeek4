package vgroup.demoweek4.camera;

import io.realm.RealmObject;

/**
 * Created by DELL on 10/7/2017.
 */

public class Photo extends RealmObject {
    String name;
    String imageUri;
    String location;


    public Photo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
