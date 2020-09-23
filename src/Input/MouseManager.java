package Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import QuadTree.QTreeVisual;

public class MouseManager implements MouseListener, MouseMotionListener {

    private  boolean leftPressed, rightPressed;
    private int mouseX, mouseY;

    QTreeVisual visualization;

    public void tick(){

    }

    public MouseManager(QTreeVisual visualization){
        this.visualization = visualization;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            leftPressed = true;
        if (e.getButton() == MouseEvent.BUTTON3)
            rightPressed = true;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            leftPressed = false;
        if (e.getButton() == MouseEvent.BUTTON3)
            rightPressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();

            synchronized(visualization){
                visualization.resetSleepCounter();                
                visualization.notify();
            }
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
