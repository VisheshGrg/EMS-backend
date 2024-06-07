package com.vishesh.task7.Config;

import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;

public class CloudinaryConfig {
	private static final String CLOUD_NAME = "dxcrwlhv0";
	private static final String API_KEY = "332342483462539";
    private static final String API_SECRET = "lgjWTLc8u5prbKT6nUQFP-4jC3w";
    
    public static Cloudinary getCloudinary() {
    	Map<String,String> config = new HashMap<>();
    	config.put("cloud_name", CLOUD_NAME);
    	config.put("api_key", API_KEY);
    	config.put("api_secret", API_SECRET);
    	return new Cloudinary(config);
    }
}
