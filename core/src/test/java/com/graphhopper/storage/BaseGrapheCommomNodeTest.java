package com.graphhopper.storage;

import com.graphhopper.util.EdgeIteratorState;
import org.junit.jupiter.api.Test;

import static com.graphhopper.util.GHUtility.getCommonNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseGrapheCommomNodeTest extends AbstractGraphStorageTester {

    @Override
    public BaseGraph createGHStorage(String location, boolean enabled3D) {
        // reduce segment size in order to test the case where multiple segments come into the game
        BaseGraph gs = newGHStorage(new RAMDirectory(location), enabled3D, defaultSize / 2);
        gs.create(defaultSize);
        return gs;
    }

    protected BaseGraph newGHStorage(Directory dir, boolean enabled3D) {
        return newGHStorage(dir, enabled3D, -1);
    }

    protected BaseGraph newGHStorage(Directory dir, boolean enabled3D, int segmentSize) {
        return new BaseGraph.Builder(encodingManager).setDir(dir).set3D(enabled3D).setSegmentSize(segmentSize).build();
    }


    /**
     * test pour getCommonNode avec deux edge qui ont un common node
     */
    @Test
    public void testCommonNodeTrue() {
        BaseGraph graph = createGHStorage();
        EdgeIteratorState edge1 = graph.edge(0, 1);  // Edge 0→1
        EdgeIteratorState edge2 = graph.edge(edge1.getBaseNode(), 2);  // Edge 1→2
        assertEquals(0, (getCommonNode(graph, edge1.getEdge(), edge2.getEdge())));

    }


    /**
     * test pour getCommonNode avec deux edge qui n'ont pas de edge en commun
     */
    @Test
    public void testCommonNodeFalse() {
        BaseGraph graph = createGHStorage();
        EdgeIteratorState edge1 = graph.edge(0, 1);  // Edge 0-1
        EdgeIteratorState edge2 = graph.edge(edge1.getBaseNode(), 2);  // Edge 0-2
        EdgeIteratorState edge3 = graph.edge(edge2.getAdjNode(),3); //2-3
        assertThrows(IllegalArgumentException.class, ()-> getCommonNode(graph, edge1.getEdge(), edge3.getEdge()));

    }


    /**
     * test insertion d'une arete circulaire 0-0
     */
    @Test
    public void insertCircularEdge() {
        BaseGraph graph = createGHStorage();
        EdgeIteratorState edge1 = graph.edge(0, 1);  // Edge 0-1
        assertThrows(IllegalArgumentException.class, ()-> graph.edge(edge1.getBaseNode(), edge1.getBaseNode())); // Edge 0-0

    }


    /**
     * test d'insertion qui forme un cercle
     */
    @Test
    public void insertEdgeThatFormACircle() {
        BaseGraph graph = createGHStorage();

        EdgeIteratorState edge1 = graph.edge(0, 1);

        EdgeIteratorState edge2 = graph.edge(0, 1);

        assertThrows(IllegalArgumentException.class, () -> getCommonNode(graph, edge1.getEdge(), edge2.getEdge()));
    }

}
