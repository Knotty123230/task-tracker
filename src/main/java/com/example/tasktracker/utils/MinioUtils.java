package com.example.tasktracker.utils;

public class MinioUtils {
    private MinioUtils() {
    }

    public static String getBucketName(String contentType) {
        switch (contentType) {
            case "image/jpeg" -> {
                return "photos";
            }
            case "video/mp4" -> {
                return "videos";
            }
            default -> {
                return "files";
            }
        }
    }

    public static String getBucketNameByFileName(String name) {
        String[] split = name.split("\\.");
        switch (split[split.length - 1]) {
            case "mp4" -> {
                return "videos";
            }
            case "jpg", "png" -> {
                return "photos";
            }
            default -> {
                return "files";
            }
        }
    }
}
