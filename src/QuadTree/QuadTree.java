package QuadTree;

import java.awt.*;
import java.util.ArrayList;

public class QuadTree {

    QNode root;
    int nodeCapacity;
    int numberOfPoints;
    Rectangle initialArea;
    ArrayList<Point> requestPoints;
    ArrayList<Point> allPoints;


    public QuadTree(int nodeCapacity, Rectangle initialArea){
        this.nodeCapacity = nodeCapacity;
        this.initialArea = initialArea;
        requestPoints = new ArrayList<>();
        allPoints = new ArrayList<>();

        root = new QNode(nodeCapacity, initialArea, requestPoints);
    }

    public void insert(Point point){
        numberOfPoints++;
        allPoints.add(point);
        root.insert(point);
    }

    public ArrayList<Point> allPointsInRectangle(Rectangle bounds) {

        if (!root.bound.intersects(bounds))
            return null;

        requestPoints.clear();
        root.allPointsInLeaf(bounds);

        return requestPoints;
    }

    public void printAllLeaves(){
        root.printLeaf();
    }
    
    public void init(){
        insert(new Point(50 ,50));
        insert(new Point(150 ,50));
        insert(new Point(50 ,150));
        insert(new Point(150 ,150));

        insert(new Point(50 ,85));
        insert(new Point(150 ,20));
        insert(new Point(50 ,15));
        insert(new Point(120 ,150));

        insert(new Point(15 ,29));
        insert(new Point(123 ,125));
        insert(new Point(0 ,0));
        insert(new Point(5 ,185));

        insert(new Point(10 ,75));
        insert(new Point(10 ,30));
        insert(new Point(2 ,7));
        insert(new Point(20 ,20));

        insert(new Point(111 ,75));
        insert(new Point(49 ,110));
        insert(new Point(21 ,17));
        insert(new Point(188 ,188));
    }

    public void renderPoints(Graphics graphics){
        graphics.setColor(Color.RED);

        for (Point p : allPoints){
            graphics.fillOval(p.x, p.y, 4, 4);
        }
    }

    public void renderRectangles(Graphics graphics){
        root.render(graphics);
    }

    public void renderQuery(Graphics graphics, Rectangle query) {
        graphics.setColor(Color.BLUE);

        graphics.drawRect(query.x, query.y, query.width, query.height);

        for (Point p : requestPoints)
            graphics.fillOval(p.x, p.y, 4, 4);

    }
}
