package org.tensorflow.lite.examples.detection.dinner.model;

import java.util.HashMap;
import java.util.Map;

public class ContentDTO {
    public String explain = null;
    public String material = null;
    public String imageUri= null;
    public String uid = null;
    public String userId = null;
    public Long timestamp = null;
    public int favoriteCount = 0;
    public Map<String,Boolean> favorites= new HashMap();
}
