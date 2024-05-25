package com.github.owly7.engine.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Image {
    public static byte[] readImage(String internalPath) {
        try (InputStream in = Image.class.getResourceAsStream(internalPath)) {
            if (in != null) {
                ByteArrayOutputStream bOut = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    bOut.write(buffer, 0, bytesRead);
                }
                return bOut.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Failed to read image: " + internalPath);
    }
}
