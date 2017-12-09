package model;

import java.util.ArrayList;

import static java.lang.Math.*;

public class TextDirections {

    public TextDirections() {

    }

    //creates text directions given an arraylist of nodes
    public String getTextDirections(ArrayList<NodeObj> pat) {
        ArrayList<NodeObj> path = new ArrayList<NodeObj>();
        for (NodeObj n : pat)
            path.add(0, n);
        ArrayList<String> msg = new ArrayList<String>();

        float angle;
        Node lastNode;
        Node curNode;
        Node nextNode;
        Node floorNode;
        Node lastStraightNode;
        String previousMsg = "";
        Double pixelToFeet = 1.5;
        Double pathLength = 0.0;

        if (path.size() == 2) {
            pathLength = (path.get(1).getDistance(path.get(0))) / pixelToFeet;
            return "go straight for " + pathLength + " feet";
        }
        for (int i = 1; i < path.size() - 1; i++) {

            lastNode = path.get(i - 1).getNode();
            curNode = path.get(i).getNode();
            nextNode = path.get(i + 1).getNode();
            floorNode = path.get(i + 1).getNode();

            // if the next node is a staircase
            if (nextNode.getNodeType().equals("STAI") && floorNode.getFloor() != nextNode.getFloor()) {
                msg.add(("Take the stairs to floor " + nextHallwayNode(path, i).getFloor() + "\n"));
            } else if (nextNode.getNodeType().equals("ELEV") && floorNode.getFloor() != nextNode.getFloor()) {
                msg.add(("Take the elevator to floor " + nextHallwayNode(path, i).getFloor() + "\n"));
            } else {
                // calculate the angle between the vector last->
                // starts from
                angle = angleBetweenNodes(lastNode, curNode, nextNode);
                // if the next node is basically straight ahead
                if (angle >= 160 || angle <= -160) {
                    if (!previousMsg.equals("go straight\n")) {
                        //Need to find next turn to get that x and y and then math out the feet
                        Node nextTurn = findNextTurn(i, path);
                        if (nextTurn != null) {
                            pathLength = distanceFormula(curNode.getxLoc(), curNode.getyLoc(), nextTurn.getxLoc(), nextTurn.getyLoc()) / pixelToFeet;
                            msg.add("go straight  for " + pathLength + " feet" + "\n");
                            previousMsg = "go straight\n";
                        }
                    }
                } else {
                    String dirAng = "";
                    // add how much to turn
                    if (Math.abs(angle) < 45)
                        dirAng += "Turn sharply ";
                    else if (Math.abs(angle) < 135)
                        dirAng += "Turn ";
                    else if (Math.abs(angle) < 160) {
                        dirAng += "Turn slightly ";
                    }

                    // add which direction to turn

                    if (angle >= 0) {
                        dirAng += "right at " + curNode.getShortName() + "\n";
                        dirAng += "go straight for " + distanceFormula(curNode.getxLoc(), curNode.getyLoc(), nextNode.getxLoc(), nextNode.getyLoc()) + " feet\n";
                        previousMsg = "right at ";

                    } else {
                        dirAng += "left at " + curNode.getShortName() + "\n";
                        dirAng += "go straight for " + distanceFormula(curNode.getxLoc(), curNode.getyLoc(), nextNode.getxLoc(), nextNode.getyLoc()) + " feet\n";
                        previousMsg = "left at ";

                    }
                    msg.add(dirAng);
                }
            }

        }
        msg.add("You have arrived at your location!");
        String txtDir = "";
        for (String s : msg)
            txtDir = txtDir + s;

        return txtDir;
    }

    //function that calculates the angles between three nodes using angle math
    int angleBetweenNodes(Node lastNode, Node curNode, Node nextNode) {

        int DxCurrLast = curNode.getxLoc() - lastNode.getxLoc();
        int DyCurrLast = -curNode.getyLoc() + lastNode.getyLoc();

        int DxNextCurr = nextNode.getxLoc() - curNode.getxLoc();
        int DyNextCurr = -nextNode.getyLoc() + curNode.getyLoc();

        double McurrLast = Math.sqrt(DxCurrLast * DxCurrLast + DyCurrLast * DyCurrLast);
        double MNextCurr = Math.sqrt(DxNextCurr * DxNextCurr + DyNextCurr * DyNextCurr);

        double DotVect = DxCurrLast * DxNextCurr + DyCurrLast * DyNextCurr;

        double angle = Math.toDegrees(Math.acos(DotVect / (McurrLast * MNextCurr)));
        if ((DxCurrLast > 0 && DyNextCurr > 0) || (DxCurrLast < 0 && DyNextCurr < 0) ||
                (DxNextCurr > 0 && DyCurrLast < 0) || (DxNextCurr < 0 && DyCurrLast > 0))
            return -180 + (int) angle;

        return 180 - (int) angle;

    }

    //function that takes in the path and gets the next hallway node
    Node nextHallwayNode(ArrayList<NodeObj> path, int start) {
        Node node = path.get(start).getNode();
        int index = 1;
        while (!node.getNodeType().equals("HALL")) {
            try {
                node = path.get(start + index).getNode();
            } catch (IndexOutOfBoundsException e) {
                return path.get(start + index - 1).getNode();
            }
            index++;
        }
        return node;
    }

    //function that calculates distance between two points.
    double distanceFormula(int Ax, int Ay, int Bx, int By) {
        double x = Math.pow((Ax - Bx), 2);
        double y = Math.pow((Ay - By), 2);
        return sqrt(x + y);
    }

    //function that finds the next turn in the path
    Node findNextTurn(int currentNode, ArrayList<NodeObj> path) {
        Node turnNode;
        float angle;
        Node lastNode;
        Node curNode;
        Node nextNode;
        for (int i = currentNode; i < path.size() - 1; i++) {
            lastNode = path.get(i - 1).getNode();
            curNode = path.get(i).getNode();
            nextNode = path.get(i + 1).getNode();
            angle = angleBetweenNodes(lastNode, curNode, nextNode);
            if (!(angle >= 160 || angle <= -160)) {
                turnNode = path.get(i).getNode();
                return turnNode;
            }
        }
        return null;
    }

}
