package Test;

import QuadTree.QuadTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.awt.*;
import java.util.ArrayList;


class QuadTreeTest {

    @Test
    void insert() {
        
        QuadTree tree = new QuadTree(20, new Rectangle(0, 0, 100 , 100));
        ArrayList<Point> inserted = new ArrayList<>();
        inserted.add(new Point(0,0));
        inserted.add(new Point(1,1));
        inserted.add(new Point(0,1));
        inserted.add(new Point(1,0));

        for (Point p : inserted)
            tree.insert(p);

        Assertions.assertEquals(inserted, tree.allPointsInRectangle(new Rectangle(0, 0, 100, 100)));
    }

    @Test
    void allPointsInRectangle() {
        ArrayList<Point> pointsInArea = new ArrayList<>();
        Rectangle area = new Rectangle(0, 0 , 100, 100);

        QuadTree tree = new QuadTree(10 , new Rectangle(0, 0 ,100 ,100));

        Assertions.assertEquals(tree.allPointsInRectangle(area), pointsInArea);
    }

    @Test
    void clear() {

        QuadTree tree = new QuadTree(20, new Rectangle(0, 0, 100 , 100));
        tree.init(100);

        // filled tree
        tree.clear();
        Assertions.assertEquals(0,  tree.size());

        // empty tree
        tree.clear();
        Assertions.assertEquals(0, tree.size());
    }

    @Test
    void loadTree() {
        QuadTree tree = new QuadTree(20, new Rectangle());

        int initializeWith = 100;

        tree.init(initializeWith);

        initializeWith += InsertMyPoints(tree);

        Assertions.assertEquals(initializeWith, tree.size());
    }

    @Test
    void getRequestPoints() {

        QuadTree qTree = new QuadTree(20, new Rectangle(0 ,0, 100, 100));

        //check if request AllArea gives all points
        Assertions.assertEquals(InsertMyPoints(qTree), qTree.allPointsInRectangle(new Rectangle(0, 0 , 100 ,100)).size());

        ArrayList<Point> a = new ArrayList<>();
        a.add(new Point(2,2));
        //check if request result for AllArea equals insertionSet
        Assertions.assertEquals(insertionSet(), qTree.allPointsInRectangle(new Rectangle(0, 0, 100, 100)));
    }

    @Test
    void getNumberOfLeafRectangles() {
        QuadTree qTree = new QuadTree(20, new Rectangle(0 ,0, 100, 100));

        //empty tree has one leaf - allArea
        Assertions.assertEquals(1, qTree.getNumberOfLeafRectangles());

        //insert less nodes than allArea nodeCapacity

        qTree.init(19);
        Assertions.assertEquals(1, qTree.getNumberOfLeafRectangles());

        //if size of node gets to nodeCapacity it should split
        qTree.insert(new Point(4,12));
        Assertions.assertEquals(4, qTree.getNumberOfLeafRectangles());

    }

    private ArrayList<Point> insertionSet(){
        ArrayList<Point> points = new ArrayList<>();

        points.add(new Point(1,1));
        points.add(new Point(2,2));
        points.add(new Point(3,3));
        points.add(new Point(4,4));
        points.add(new Point(5,5));
        points.add(new Point(6, 6));
        points.add(new Point(16, 69));
        points.add(new Point(13 ,13));
        points.add(new Point(12 ,13));
        points.add(new Point(50, 69));
        points.add(new Point(5, 10));
        points.add(new Point(11 , 11));
        points.add(new Point(40 , 20));
        points.add(new Point(50 , 89));
        points.add(new Point(60, 60));
        points.add(new Point(70, 70));
        points.add(new Point(80 ,80));

        return points;
    }

    public int InsertMyPoints(QuadTree qTree){
        ArrayList<Point> points = insertionSet();

        for (Point p : points)
            qTree.insert(p);

        return points.size();
    }
}