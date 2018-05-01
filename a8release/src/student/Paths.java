package student;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import models.Edge;
import models.Node;

/** This class contains the shortest-path algorithm and other methods. */
public class Paths {

    /** Return the shortest path from start to end ---or the empty list
     * if a path does not exist.
     * Note: The empty list is NOT "null"; it is a list with 0 elements. */
    public static List<Node> minPath(Node start, Node end) {
        /* TODO Read Piazza note Assignment A7 for ALL details. */
        Heap<Node> F= new Heap<Node>(true); // As in lecture slides

        // no path from start to end
        return new LinkedList<Node>();
    }


    /** Return the path from the start node to node end.
     *  Precondition: data contains all the necessary information about
     *  the path. */
    public static List<Node> makePath(HashMap<Node, SF> data, Node end) {
        List<Node> path= new LinkedList<Node>();
        Node p= end;
        // invariant: All the nodes from p's successor to the end are in
        //            path, in reverse order.
        while (p != null) {
            path.add(0, p);
            p= data.get(p).backPtr;
        }
        return path;
    }

    /** Return the sum of the weights of the edges on path p. */
    public static int pathWeight(List<Node> p) {
        if (p.size() == 0) return 0;
        synchronized(p) {
            Iterator<Node> iter= p.iterator();
            Node v= iter.next();  // First node on path
            int sum= 0;
            // invariant: s = sum of weights of edges from start to v
            while (iter.hasNext()) {
                Node q= iter.next();
                sum= sum + v.getEdge(q).length;
                v= q;
            }
            return sum;
        }
    }

    /** An instance contains information about a node: the previous node
     *  on a shortest path from the start node to this node and the distance
     *  of this node from the start node. */
    private static class SF {
        private Node backPtr; // backpointer on path from start node to this one
        private int distance; // distance from start node to this one

        /** Constructor: an instance with backpointer p and
         * distance d from the start node.*/
        private SF(Node p, int d) {
            distance= d;     // Distance from start node to this one.
            backPtr= p;  // Backpointer on the path (null if start node)
        }

        /** return a representation of this instance. */
        public String toString() {
            return "dist " + distance + ", bckptr " + backPtr;
        }
    }
}
