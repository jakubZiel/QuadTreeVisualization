package QuadTree;

import java.awt.*;
import java.util.ArrayList;


public class QNode {

     QuadTree mainTree;

     QNode TopLeft;
     QNode TopRight;
     QNode BottomLeft;
     QNode BottomRight;

     ArrayList<Point> result;
     ArrayList<Point> pointsIn;

     int capacity;
     boolean split;
     Rectangle bound;
    

     QNode(int capacity, Rectangle bound, ArrayList<Point> requestPoints, QuadTree qTree){
         this.bound = bound;
         this.capacity = capacity;
         pointsIn = new ArrayList<>();
         this.result = requestPoints;
         this.mainTree = qTree;
      
         split = false;
     }

     void insert(Point point){
         if (split) {
             if (TopLeft.bound.contains(point)) TopLeft.insert(point);
             else if (TopRight.bound.contains(point)) TopRight.insert(point);
             else if (BottomLeft.bound.contains(point)) BottomLeft.insert(point);
             else if (BottomRight.bound.contains(point)) BottomRight.insert(point);
             return;
         }

        if (pointsIn.size() == capacity - 1) {
            pointsIn.add(point);
            split = true;
            split();
        }else
            pointsIn.add(point);
    }

     private void split(){
        

        TopLeft = new QNode(capacity, new Rectangle(bound.x, bound.y, floorDiv2(bound.width), floorDiv2(bound.height)), result, this.mainTree);
        BottomLeft = new QNode(capacity, new Rectangle(bound.x, bound.y + bound.height / 2, floorDiv2(bound.width), floorDiv2(bound.height)), result, this.mainTree);
        TopRight = new QNode(capacity,new Rectangle(bound.x + bound.width / 2, bound.y, floorDiv2(bound.width), floorDiv2(bound.height)), result, this.mainTree);
        BottomRight = new QNode(capacity, new Rectangle(bound.x + bound.width / 2, bound.y + bound.height / 2, floorDiv2(bound.width), floorDiv2(bound.height)), result, this.mainTree);

        for (Point point : pointsIn){
            if (TopLeft.bound.contains(point)) TopLeft.insert(point);
            else if (TopRight.bound.contains(point)) TopRight.insert(point);
            else if (BottomLeft.bound.contains(point)) BottomLeft.insert(point);
            else if (BottomRight.bound.contains(point)) BottomRight.insert(point);
        }
        pointsIn.clear();
    }

     void printLeaf(){
         if (split){
             TopLeft.printLeaf();
             TopRight.printLeaf();
             BottomRight.printLeaf();
             BottomLeft.printLeaf();

         }else{
             System.out.print(bound);
             for (Point p : pointsIn)
                 System.out.println(p);

             System.out.println();
         }
     }

     void allPointsInLeaf(Rectangle bound){
         if (!split) {

             this.mainTree.comparisions += pointsIn.size();
             for (Point p : pointsIn)
                 if (bound.contains(p))
                     result.add(p);
         }else {
             if (bound.intersects(BottomLeft.bound))
                 BottomLeft.allPointsInLeaf(bound);
             if (bound.intersects(BottomRight.bound))
                 BottomRight.allPointsInLeaf(bound);
             if (bound.intersects(TopLeft.bound))
                 TopLeft.allPointsInLeaf(bound);
             if (bound.intersects(TopRight.bound))
                 TopRight.allPointsInLeaf(bound);
         }

     }

     public static int floorDiv2(int number){
        if (number % 2 == 0) return number / 2;
        else
            return number / 2 + 1;
    }

     public void render(Graphics graphics){
         if (split) {
             BottomRight.render(graphics);
             BottomLeft.render(graphics);
             TopRight.render(graphics);
             TopLeft.render(graphics);
             return;
         }else
             graphics.drawRect(bound.x, bound.y, bound.width, bound.height);
     }

     public void deleteNode() {
        if (split) {

            TopLeft.deleteNode();
            TopRight.deleteNode();
            BottomRight.deleteNode();
            BottomLeft.deleteNode();

            TopLeft = null;
            TopRight = null;
            BottomLeft = null;
            BottomRight = null;

            System.gc();

        } else {
            pointsIn.clear();
        }
    }

    public int checkNumberOfRectangles(){
        int sumOfChildren = 0;
        
        if (!split)
            return 1;
        else {
            sumOfChildren += this.TopLeft.checkNumberOfRectangles();
            sumOfChildren += this.TopRight.checkNumberOfRectangles();
            sumOfChildren += this.BottomLeft.checkNumberOfRectangles();
            sumOfChildren += this.BottomRight.checkNumberOfRectangles();
        }

        return sumOfChildren;
    }
}
