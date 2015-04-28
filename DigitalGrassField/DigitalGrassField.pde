
ArrayList<PVector> origins = new ArrayList<PVector>();

void setup() {
    size(displayWidth, displayHeight, P3D);
    origins = getNoiseSample(0.1, 0.38, 10); // get samples
}

void draw() {
    colorMode(RGB);
    background(0);
    stroke(100);

    float fov = PI/2.0;
    float cameraZ = (height/2.0) / tan(fov/2.0);
    float aspect = float(width)/float(height);
    float zNear = cameraZ/69.2;
    float zFar = cameraZ/1.0;
    perspective(fov, aspect, zNear, zFar);

    beginCamera();
    camera();
    rotateX(PI/3.73);
    translate(0, 351, 157);
    endCamera();

    int len = 13; // length of grass
    int amp = 7; // amplitude of vibrations
    int hill = 11; // amplitude of height
    float nR = 0.01; // noise for length
    float aR = 0.02; // noise for vibration
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
        fill(color(236, 0.21, pow(noise(x*aR, frameCount*aR), 1.7) + pow(noise(y*aR, frameCount*aR), 1.5)));

        // triangles
        beginShape(TRIANGLES);
        vertex(x, y, hillShift);
        vertex(x + 0.5, y + 0.5, hillShift);
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


ArrayList<PVector> getNoiseSample(float noiseScale, float noiseThreshold, int gridStep) {
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
                float angle =  6.28 * random(1);
                float r =  gridStep * random(1);
                float pointX = x + r * cos(angle);
                float pointY = y + r * sin(angle);
                points.add(new PVector(pointX, pointY));
            }
        }
    }
    return points;
}

