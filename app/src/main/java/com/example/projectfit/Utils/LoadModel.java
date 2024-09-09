package com.example.projectfit.Utils;

import org.tensorflow.lite.Interpreter;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.io.FileInputStream;
import java.io.IOException;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

public class LoadModel {
    private Interpreter tflite;

    public LoadModel(AssetManager assetManager, String modelPath) throws IOException {
        tflite = new Interpreter(loadModelFile(assetManager, modelPath));
    }

    private MappedByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor = assetManager.openFd(modelPath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public float predictMaxSteps(int gender, float height, float weight, int age) {
        float[][] input = new float[1][4];
        input[0][0] = gender;
        input[0][1] = height;
        input[0][2] = weight;
        input[0][3] = age;

        float[][] output = new float[1][1];
        tflite.run(input, output);
        return output[0][0];
    }

    public float predictMaxWater(int gender, float height, float weight, int age) {
        float[][] input = new float[1][4];
        input[0][0] = gender;
        input[0][1] = height;
        input[0][2] = weight;
        input[0][3] = age;

        float[][] output = new float[1][1];
        tflite.run(input, output);
        return output[0][0];
    }
}
