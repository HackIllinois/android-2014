package org.hackillinois.android.models.Support;

public class Support {

private String title;

/*NOTE In this implementation a support model could refer to a category, a sub category, or a room (room number)*/

public Support(String title) {
    this.title = title;
}



public String getTitle() {
   return title;
}

public void setTitle(String title) {
        this.title = title;
}
}