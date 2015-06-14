import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DigitalGrassField extends PApplet {


ArrayList<PVector> origins = new ArrayList<PVector>();

public void setup() {
    size(displayWidth, displayHeight, P3D);
    origins = getNoiseSample(0.1f, 0.38f, 10); // get samples
}

public void draw() {
    colorMode(RGB);
    background(0);
    stroke(100);

    float fov = PI/2.0f;
    float cameraZ = (height/2.0f) / tan(fov/2.0f);
    float aspect = PApplet.parseFloat(width)/PApplet.parseFloat(height);
    float zNear = cameraZ/69.2f;
    float zFar = cameraZ/1.0f;
    perspective(fov, aspect, zNear, zFar);

    beginCamera();
    camera();
    rotateX(PI/3.73f);
    translate(0, 351, 157);
    endCamera();

    int len = 13; // length of grass
    int amp = 7; // amplitude of vibrations
    int hill = 11; // amplitude of height
    float nR = 0.01f; // noise for length
    float aR = 0.02f; // noise for vibration
    for (int i = 0; i < origins.size (); i++) {
        float x = origins.get(i).x;
        float y = origins.get(i).y;
        float xShift = amp * noise(x*aR, frameCount*aR);
        float yShift = amp * noise(y*aR, frameCount*aR);
        float hillShift = hill * noise(x*nR, y*nR);
        float l = len * noise(x*nR, y*nR, frameCount*nR);

        //line(x, y, 0, x + xShift, y + yShift, l);

        // colorize
        colorMode(HSB, 360, 1, 1);
        noStroke();
        fill(color(236, 0.21f, pow(noise(x*aR, frameCount*aR), 1.7f) + pow(noise(y*aR, frameCount*aR), 1.5f)));

        // triangles
        beginShape(TRIANGLES);
        vertex(x, y, hillShift);
        vertex(x + 0.5f, y + 0.5f, hillShift);
        vertex(x + xShift, y + yShift, l +  hillShift);
        endShape();

        //        pushMatrix();
        //        translate(x, y, 0);
        //        //rotateX(asin((xShift)/l));
        //        //rotateY(asin((yShift)/l));
        //        box(2, 2, l);
        //        popMatrix();
    }
}


public ArrayList<PVector> getNoiseSample(float noiseScale, float noiseThreshold, int gridStep) {
    /** Return an array of 2D-samples within display bounds
     *  noiseScale     - scale for Perlin noise
     *  noiseThreshold - threshold for setting on a sample
     *  gridStep       - step for traversing width and height
     */
    ArrayList<PVector> points = new ArrayList<PVector>();

    for (int y = 0; y < height; y += gridStep) {
        for (int x = 0; x < width; x += gridStep) {
            float test = noise(x, y);
            if (test > noiseThreshold) {
                float angle =  6.28f * random(1);
                float r =  gridStep * random(1);
                float pointX = x + r * cos(angle);
                float pointY = y + r * sin(angle);
                points.add(new PVector(pointX, pointY));
            }
        }
    }
    return points;
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "DigitalGrassField" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
