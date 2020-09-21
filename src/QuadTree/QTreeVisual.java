package QuadTree;

import Input.KeyManager;
import Input.MouseManager;
import display.Display;
import java.awt.*;
import java.awt.image.BufferStrategy;


public class QTreeVisual {

    private Display display;
    public QuadTree qTreeModel;
    private MouseManager mouseManager;
    private KeyManager keyManager;

    private BufferStrategy buffStrategy;
    private Graphics graphics;
    private int height;
    private int width;
    private Rectangle requestArea;

    public QTreeVisual(QuadTree qTreeModel, int width, int height){
        this.height = height;
        this.width = width;
        this.mouseManager = new MouseManager();
        this.keyManager = new KeyManager();

        this.display = new Display("QuadTree", width , height + 38 );


        display.getFrame().addKeyListener(keyManager);
        display.getCanvas().addKeyListener(keyManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);

        this.qTreeModel = qTreeModel;
    }

    public void render(Rectangle queryBounds){
        buffStrategy = display.getCanvas().getBufferStrategy();

        if(buffStrategy == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        graphics = buffStrategy.getDrawGraphics();
        graphics.clearRect(0,0, width, height);

        qTreeModel.renderRectangles(graphics);
        qTreeModel.renderPoints(graphics);

        if (queryBounds != null){
            qTreeModel.allPointsInRectangle(queryBounds);
            qTreeModel.renderQuery(graphics,queryBounds);
        }

        buffStrategy.show();
        graphics.dispose();
    }

    public void tick(){
        keyManager.tick();
        takeInput();
        requestArea.setBounds(mouseManager.getMouseX() - requestArea.width / 2, mouseManager.getMouseY() - requestArea.height / 2, requestArea.width, requestArea.height);
    }

    private void takeInput(){
        if (keyManager.height) {
            if (keyManager.plus && requestArea.height < height) {
                requestArea.setSize(requestArea.width, requestArea.height + 1);
            } else if (keyManager.minus && height > 5) {
                requestArea.setSize(requestArea.width, requestArea.height - 1);
            }
        }

        if (keyManager.width){
            if (keyManager.plus && requestArea.width < width) {
                requestArea.setSize(requestArea.width + 1, requestArea.height);
            }else if (keyManager.minus && requestArea.width > 5){
                requestArea.setSize(requestArea.width - 1, requestArea.height);
            }
        }
    }

    public void renderLoop(){

        int fps = 120;
        double timePerTick = 1000000000.0f/ fps;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        requestArea = new Rectangle(15,29, 100,49);

        while(true){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render(requestArea);
                delta--;
                ticks++;
            }
            if(timer >= 1000000000) {
                System.out.println("ticks: " + ticks);
                System.out.println(requestArea);
                timer = 0;
                ticks = 0;
            }
        }
    }

    public static void main(String[] args) {
        QuadTree quadTree = new QuadTree(4, new Rectangle(0,0, 800,600));
        quadTree.init();

        QTreeVisual qTreeVisual = new QTreeVisual(quadTree, quadTree.initialArea.width, quadTree.initialArea.height);
        qTreeVisual.renderLoop();

    }
}
