package Test;

import QuadTree.QNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

class QNodeTest {

    @Test
    void insert() {
        int capacity = 4;
        Rectangle area = new Rectangle(0, 0 ,100 ,100);
        QNode node = new QNode(capacity, area,null, null);



        node.insert(new Point(0, 0));
        node.insert(new Point(0, 1));
        node.insert(new Point(1, 0));

        Assertions.assertEquals(node.getPointsIn().size() ,3);

        node.insert(new Point(1, 1));
        Assertions.assertEquals(node.getPointsIn().size() ,0);
    }
}