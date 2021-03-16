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
    int comparisons;
    int leafRectangles;

    public QuadTree(int nodeCapacity, Rectangle initialArea){
        this.nodeCapacity = nodeCapacity;
        this.initialArea = initialArea;
        requestPoints = new ArrayList<>();
        allPoints = new ArrayList<>();

        root = new QNode(nodeCapacity, initialArea, requestPoints, this);
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
        comparisons = 0;
        root.allPointsInLeaf(bounds);

        return requestPoints;
    }

    public void printAllLeaves(){
        root.printLeaf();
    }
    
    public void init(int numberOfPoints){
        for (Point p : QuadTree.randomPoints(numberOfPoints, this.initialArea.width, this.initialArea.height))
            this.insert(p);

        leafRectangles = getNumberOfLeafRectangles();
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

    public void clear(){
        numberOfPoints = 0;
        root.deleteNode();
        root.split = false;
    }

    public void loadTree(ArrayList<Point> points){

        for (Point p: points)
            this.insert(p);

        System.out.println("Tree Loaded");
    }

    public static ArrayList<Point> randomPoints(int numberOfPoints, int maxX, int maxY){
        ArrayList<Point> points = new ArrayList<>();

        for (int i = 0; i < numberOfPoints; i++)
            points.add(new Point((int)( Math.random() * maxX), (int) (Math.random() * maxY)));

        return points;
    }

    public ArrayList<Point> getRequestPoints(){
        return requestPoints;
    }

    public int getNumberOfLeafRectangles(){
        return root.checkNumberOfRectangles();
    }

    public int size() {
        return numberOfPoints;
    }
}
