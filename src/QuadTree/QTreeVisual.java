package QuadTree;

import Input.KeyManager;
import Input.MouseManager;
import display.Display;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class QTreeVisual implements Runnable {

    private Display display;
    public QuadTree qTreeModel;
    private MouseManager mouseManager;
    private KeyManager keyManager;

    private BufferStrategy buffStrategy;
    private Graphics graphics;
    private int height;
    private int width;
    private Rectangle requestArea;
    private int numberOfAllPoints;
    private int sleepCounter;

    public QTreeVisual(QuadTree qTreeModel) {
        this.height = qTreeModel.initialArea.height;
        this.width = qTreeModel.initialArea.width;
        this.mouseManager = new MouseManager(this);
        this.keyManager = new KeyManager();

        this.display = new Display("QuadTree", width + 250, height + 38);

        display.getFrame().addKeyListener(keyManager);
        display.getCanvas().addKeyListener(keyManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);

        this.qTreeModel = qTreeModel;
    }

    public void render(Rectangle queryBounds) {
        buffStrategy = display.getCanvas().getBufferStrategy();

        if (buffStrategy == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        graphics = buffStrategy.getDrawGraphics();
        graphics.clearRect(0, 0, width + 250, height + 38);

        qTreeModel.renderRectangles(graphics);
        qTreeModel.renderPoints(graphics);
        renderStats(graphics);
        renderHints(graphics);

        if (queryBounds != null) {
            qTreeModel.allPointsInRectangle(queryBounds);
            qTreeModel.renderQuery(graphics, queryBounds);
        }

        buffStrategy.show();
        graphics.dispose();
    }

    public void tick() {
        keyManager.tick();
        takeInput();
        requestArea.setBounds(mouseManager.getMouseX() - requestArea.width / 2,
                mouseManager.getMouseY() - requestArea.height / 2, requestArea.width, requestArea.height);
    }

    private void takeInput() {
        if (keyManager.height) {
            if (keyManager.plus && requestArea.height < height) {
                requestArea.setSize(requestArea.width, requestArea.height + 1);
            } else if (keyManager.minus && requestArea.height > 5) {
                requestArea.setSize(requestArea.width, requestArea.height - 1);
            }
        }

        if (keyManager.width) {
            if (keyManager.plus && requestArea.width < width) {
                requestArea.setSize(requestArea.width + 1, requestArea.height);
            } else if (keyManager.minus && requestArea.width > 5) {
                requestArea.setSize(requestArea.width - 1, requestArea.height);
            }
        }
    }

    public void renderLoop() {

        int fps = 120;
        double timePerTick = 1000000000.0f / fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;

        requestArea = new Rectangle(15, 29, 100, 49);

        while (true) {
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (sleepCounter == 5)
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            if (delta >= 1) {
                tick();
                render(requestArea);
                delta--;
                sleepCounter++;
            }
            if (timer >= 1000000000)
                System.out.println(requestArea);

        }
    }

    private void renderStats(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(graphics.getFont().deriveFont(15f));

        graphics.drawString("area width:" + requestArea.getWidth(), 810, 300);
        graphics.drawString("area height:" + requestArea.getHeight(), 810, 330);
        graphics.drawString("points in area:" + qTreeModel.getRequestPoints().size(), 810, 360);
        graphics.drawString("All points:" + numberOfAllPoints, 810, 390);
        graphics.drawString("comparisions: " + this.qTreeModel.comparisions, 810, 420);
        graphics.drawString("rectangles: " + this.qTreeModel.leafRectangles, 810, 450);
        graphics.setColor(Color.RED);
        graphics.drawString("classicSearch comparisions:" + numberOfAllPoints, 810, 480);
    }

    private void renderHints(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.setFont(graphics.getFont().deriveFont(15f));

        graphics.drawString("Toggle height 'H' ", 810, 50);
        graphics.drawString("Toggle width 'W' ", 810, 80);
        graphics.drawString("inc/dec press 'P'/'M' ", 810, 110);

    }

    public void init(int numberOfPoints) {
        this.qTreeModel.init(numberOfPoints);
        this.numberOfAllPoints = numberOfPoints;
    }

    public void randomInit(int maxNumberOfPoints) {

        int randomNumb = (int) (maxNumberOfPoints * Math.random());

        this.qTreeModel.init(randomNumb);
        this.numberOfAllPoints = randomNumb;
    }

    @Override
    public void run() {
        init(1000);
        renderLoop();
    }

    public static void main(String[] args) {

        Thread visualization = new Thread(new QTreeVisual(new QuadTree(15, new Rectangle(0, 0, 800, 600))));
        visualization.start();

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0 ; i < 5 ; i++){
            threads.add(new Thread(new QTreeVisual(new QuadTree(15, new Rectangle(0, 0, 800, 600)))));
            threads.get(i).start();
        }

    }

    public void resetSleepCounter(){
        this.sleepCounter = 0;
    }
}
