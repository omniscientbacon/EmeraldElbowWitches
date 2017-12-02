package model;

import java.net.SocketPermission;
import java.util.ArrayList;
import java.util.LinkedList;

public class BreadthFirst extends PathingAlgorithm {
    private ArrayList<NodeObj> GenPath;

    /**
     * Breadth first search using a stack that takes in a start node and goal node. It goes until it runs out of nodes to explore, or the popped node is the goal.
     * @param start Origin node for the path.
     * @param goal Destination node for the path.
     * @return boolean Returns true when path is complete.
     */
    public boolean pathfind(NodeObj start, NodeObj goal) {
        ArrayList<NodeObj> explored = new ArrayList<NodeObj>(); // List of explored nodes, or nodes that have been accounted for
        LinkedList<NodeObj> queue = new LinkedList<NodeObj>();  // List of nodes that have not been explored yet
        queue.add(start);
        explored.add(start);
        while (!queue.isEmpty()) {
            NodeObj current = queue.remove();
            if (current.node.getNodeID().equals(goal.node.getNodeID())) {
                explored.add(current);
                GenPath = constructPath(goal, start);
                return true;
            }
            ArrayList<NodeObj> neighbours = current.getListOfNeighbors();
            for (int i = 0; i < neighbours.size(); i++) {
                NodeObj n = neighbours.get(i);
                if (n != null && !explored.contains(n)) {
                    queue.add(n);
                    explored.add(n);
                    n.setParent(current);
                }
            }
        }
        return false;
    }

    //getter
    public ArrayList<NodeObj> getGenPath() {
        System.out.println("BREADTH FIRST");
        return GenPath;
    }
}
