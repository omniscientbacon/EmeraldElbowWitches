package controller;

import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminController extends Controller {

    @FXML
    private JFXButton serviceRequestBtn, addEditBtn, RemoveButton, editEmployeesBtn, removeNodeBtn, btn_map03, btn_map02,
            btn_map01, btn_mapG, btn_mapL1, btn_mapL2, astarBtn, depthBtn, breadthBtn, dijkstrasBtn, bestBtn, beamBtn,
            Tleft, Tright, Tup, Tdown;

    @FXML
    private JFXTextField nodeTypeField, shortNameField, longNameField, teamField, buildingField, floorField, yLocField,
            xLocField, nodeIDField, weightField, nodeBField, nodeAField;

    @FXML
    private JFXSlider zoomBar, pathSpeed;

    @FXML
    private Canvas gc;

    @FXML
    private JFXComboBox<String> CurrRequ;

    @FXML
    public ImageView currentMap;

    @FXML
    private Pane nodeInfoPane, edgeInfoPane, masterPane;

    @FXML
    private JFXToggleButton nodeAlignmentToggle;

    public static TextDirections textDirections = new TextDirections();
    ArrayList<NodeObj> currPath = null;
    NodeObj goal = null;
    public String servReqNodeID;
    NodeObj nodeA = null;
    private SingleController single = SingleController.getController();
    private ImageLoader mapImage = new ImageLoader();
    private GraphicsContext gc1 = null;

    public void initialize() {
        Image m1 = mapImage.getLoadedMap("btn_map01");
        currentMap.setImage(m1);
        single.getAlgorithm().setPathAlg(new astar());
        single.setMapWidth(currentMap.getFitWidth());
        single.setMapHeight(currentMap.getFitHeight());
        astarBtn.setStyle("-fx-background-color: #4286f4");
        redraw();
        setFxmlMouseKeyboardEvent();
    }

    // modifies all elements of the admin screen to call resetTimeoutCounter
    // this is required for the admin auto-logout feature in autoLogoutHelper
    void setFxmlMouseKeyboardEvent() {
        for (javafx.scene.Node n : masterPane.getChildren()) {
            n.setOnMousePressed(event -> resetTimeoutCounter());
            n.setOnKeyPressed(event -> resetTimeoutCounter());
        }
        for (javafx.scene.Node n : nodeInfoPane.getChildren()) {
            n.setOnMousePressed(event -> resetTimeoutCounter());
            n.setOnKeyPressed(event -> resetTimeoutCounter());
        }
        for (javafx.scene.Node n : edgeInfoPane.getChildren()) {
            n.setOnMousePressed(event -> resetTimeoutCounter());
            n.setOnKeyPressed(event -> resetTimeoutCounter());
        }
    }

    private void redraw() {
        nodeInfoPane.setVisible(false);
        edgeInfoPane.setVisible(false);
        serviceRequestBtn.setVisible(false);
        nodeIDField.clear();
        xLocField.clear();
        yLocField.clear();
        floorField.clear();
        nodeTypeField.clear();
        teamField.clear();
        buildingField.clear();
        longNameField.clear();
        shortNameField.clear();
        weightField.clear();
        nodeAField.clear();
        nodeBField.clear();
        if (gc1 == null)
            gc1 = gc.getGraphicsContext2D();
        gc1.clearRect(0, 0, currentMap.getFitWidth(), currentMap.getFitHeight());
        gc1.setLineWidth(2);
        gc1.setFill(Color.BLACK);
        for (NodeObj n : Main.getNodeMap().getFilteredNodes()) {
            for (EdgeObj e : n.getListOfEdgeObjs()) {
                gc1.setStroke(Color.BLUE);
                gc1.strokeLine(e.getNodeA().node.getxLoc() * single.getMapWidth() / 5000,
                        e.getNodeA().node.getyLoc() * single.getMapHeight() / 3400,
                        e.getNodeB().node.getxLoc() * single.getMapWidth() / 5000,
                        e.getNodeB().node.getyLoc() * single.getMapHeight() / 3400);
                /*gc1.fillText("" + e.getWeight(),
                        (e.getNodeA().node.getxLoc()*mapWidth/5000 +e.getNodeB().node.getxLoc()*mapWidth/5000)/2,
                        (e.getNodeA().node.getyLoc()*mapHeight/3400+ e.getNodeB().node.getyLoc()*mapHeight/3400)/2);*/
            }

        }

        for (NodeObj n : Main.getNodeMap().getFilteredNodes()) {
            gc1.setFill(Color.BLACK);
            gc1.fillOval(n.node.getxLoc() * single.getMapWidth() / 5000 - 5,
                    n.node.getyLoc() * single.getMapHeight() / 3400 - 5,
                    10,
                    10);
            gc1.setFill(Color.LIGHTBLUE);
            gc1.fillOval(n.node.getxLoc() * single.getMapWidth() / 5000 - 4,
                    n.node.getyLoc() * single.getMapHeight() / 3400 - 4,
                    8,
                    8);
        }
        gc1.setFill(Color.BLUE);
    }

    @FXML
    void changeMap(Event e) {
        Main.controllers.updateAllMaps(e);
    }


    void getMap(Event e) {
        btn_mapL2.setStyle("-fx-background-color:  #4286f4");
        btn_mapL1.setStyle("-fx-background-color:  #4286f4");
        btn_mapG.setStyle("-fx-background-color:  #4286f4");
        btn_map01.setStyle("-fx-background-color:  #4286f4");
        btn_map02.setStyle("-fx-background-color:  #4286f4");
        btn_map03.setStyle("-fx-background-color:  #4286f4");

        String clickedID = ((JFXButton) e.getSource()).getId();
        switch (clickedID) {
            case "btn_mapL2":
                Main.getNodeMap().setCurrentFloor("L2");
                btn_mapL2.setStyle("-fx-background-color:  #1b5cc4");

                break;
            case "btn_mapL1":
                Main.getNodeMap().setCurrentFloor("L1");
                btn_mapL1.setStyle("-fx-background-color:  #1b5cc4");

                break;
            case "btn_mapG":
                Main.getNodeMap().setCurrentFloor("G");
                btn_mapG.setStyle("-fx-background-color:  #1b5cc4");

                break;
            case "btn_map01":
                Main.getNodeMap().setCurrentFloor("1");
                btn_map01.setStyle("-fx-background-color:  #1b5cc4");

                break;
            case "btn_map02":
                Main.getNodeMap().setCurrentFloor("2");
                btn_map02.setStyle("-fx-background-color:  #1b5cc4");

                break;
            case "btn_map03":
                Main.getNodeMap().setCurrentFloor("3");
                btn_map03.setStyle("-fx-background-color:  #1b5cc4");
                break;

        }
        gc1.clearRect(0, 0, currentMap.getFitWidth(), currentMap.getFitHeight());
        Image map = mapImage.getLoadedMap(clickedID);
        this.currentMap.setImage(map);
        redraw();
    }


    @FXML
    void clickHandler(MouseEvent event) throws InvalidNodeException {
        int mousex = (int) ((5000 * event.getX()) / single.getMapWidth());
        int mousey = (int) ((3400 * event.getY()) / single.getMapHeight());
        if ((event.getButton() == MouseButton.SECONDARY) || ((event.getButton() == MouseButton.PRIMARY) && (event.isControlDown()))) {
            redraw();
            createNewNode(mousex, mousey);
        } else if (event.getButton() == MouseButton.PRIMARY) {
            if (nodeA == null) {
                selectNodeA(event);
            } else {
                if (nodeA.getNode().getNodeID().equals(Main.getNodeMap().getNearestNeighborFilter(mousex, mousey).getNode().getNodeID())) {
                    edgeInfoPane.setVisible(false);
                    selectNode(mousex, mousey);
                    nodeA = null;
                } else {
                    nodeInfoPane.setVisible(false);
                    selectEdge(event);
                    nodeA = null;

                }
            }
        }
    }

    @FXML
    void createNewNode(int mousex, int mousey) {
        nodeIDField.setText("");
        xLocField.setText(mousex + "");
        yLocField.setText(mousey + "");
        floorField.setText(Main.getNodeMap().currentFloor);
        teamField.setText("Team E");
        //now make all fields visible with .setVisibility(true)
        nodeInfoPane.setVisible(true);
    }

    @FXML
    void selectNode(int mousex, int mousey) {
        try {
            NodeObj editNode = Main.getNodeMap().getNearestNeighborFilter(mousex, mousey);

            //set each of the fields to their values
            nodeIDField.setText(editNode.getNode().getNodeID());
            xLocField.setText(editNode.getNode().getxLoc() + "");
            yLocField.setText(editNode.getNode().getyLoc() + "");
            floorField.setText(editNode.getNode().getFloor());
            nodeTypeField.setText(editNode.getNode().getNodeType());
            teamField.setText(editNode.getNode().getTeam());
            buildingField.setText(editNode.getNode().getBuilding());
            longNameField.setText(editNode.getNode().getLongName());
            shortNameField.setText(editNode.getNode().getShortName());

            //now make all fields visible with .setVisibility(true)
            nodeInfoPane.setVisible(true);
            serviceRequestBtn.setVisible(true);

        } catch (InvalidNodeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void removeNode() {
        String delNodeID = nodeIDField.getText();
        Node delNode = new Node(delNodeID); //WARNING: THIS CREATES A Node WITH ONLY AN ID, NO OTHER FIELDS POPULATED. ONLY ATTEMPT TO ACCESS nodeID.
        NodeObj delNodeObj = new NodeObj(delNode);
        Main.getNodeMap().removeNode(delNodeObj);
        redraw();
    }

    @FXML
    void addEditNode() {
        String xLoc = xLocField.getText();
        String yLoc = yLocField.getText();
        String floor = floorField.getText();
        String building = buildingField.getText();
        String nodeType = nodeTypeField.getText();
        String longName = longNameField.getText();
        String shortName = shortNameField.getText();
        String team = teamField.getText();
        String nodeID = nodeIDField.getText();
        Node modNode = new Node(xLoc, yLoc, floor, building, nodeType, longName, shortName, team, nodeID);
        NodeObj modNodeObj = new NodeObj(modNode);
        try {
            Main.getNodeMap().addEditNode(modNodeObj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        redraw();
    }

    @FXML
    void selectNodeA(MouseEvent event) {
        nodeInfoPane.setVisible(false);
        int mousex = (int) ((5000 * event.getX()) / single.getMapWidth());
        int mousey = (int) ((3400 * event.getY()) / single.getMapHeight());
        try {
            nodeA = Main.getNodeMap().getNearestNeighborFilter(mousex, mousey);
            nodeAField.setText(nodeA.getNode().getNodeID());
        } catch (InvalidNodeException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void selectEdge(MouseEvent event) {
        System.out.println("DRAG RELEASED");

        int mousex = (int) ((5000 * event.getX()) / single.getMapWidth());
        int mousey = (int) ((3400 * event.getY()) / single.getMapHeight());

        try {
            NodeObj nodeEnd = Main.getNodeMap().getNearestNeighborFilter(mousex, mousey);
            String nodeB = nodeEnd.getNode().getNodeID();
            nodeBField.setText(nodeB);

            if (nodeAlignmentToggle.isSelected()) {
                System.out.println("We're aligning!");
                NodeAlignment nodeAlign = new NodeAlignment();

                nodeAlign.nodeAlignment(nodeA, nodeEnd);
                ArrayList<NodeObj> nodesToBeChanged = nodeAlign.getChangedNodes();

                for (NodeObj node : nodesToBeChanged) {
                    Main.getNodeMap().addEditNode(node);
                }
                redraw();

            } else if (nodeA.getEdgeObj(Main.getNodeMap().getNearestNeighborFilter(mousex, mousey)) != null) {
                weightField.setText(nodeA.getEdgeObj(Main.getNodeMap().getNearestNeighborFilter(mousex, mousey)).getWeight() + "");
            } else {
                weightField.setText("1");
            }
            edgeInfoPane.setVisible(true);
        } catch (InvalidNodeException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
        /*
         *UpdateBorderButton is called when adding or editing. It updates the
         * node map, and redraws the screen
         */
    void addEditEdge() {
        String NIDA = nodeAField.getText();
        String NIDB = nodeBField.getText();
        int eWeight = Integer.parseInt(weightField.getText());
        EdgeObj edgeAB = Main.getNodeMap().addEditEdge(NIDA, NIDB, eWeight);
        redraw();

        gc1.setStroke(Color.RED);
        gc1.strokeLine(edgeAB.getNodeA().node.getxLoc() * single.getMapWidth() / 5000,
                edgeAB.getNodeA().node.getyLoc() * single.getMapHeight() / 3400,
                edgeAB.getNodeB().node.getxLoc() * single.getMapWidth() / 5000,
                edgeAB.getNodeB().node.getyLoc() * single.getMapHeight() / 3400);

    }

    /*
     * DeleteBorderButton is called when deleting an edge. Removes edge from map, and redraws screen
     */
    @FXML
    void removeEdge() {
        String nodeA = nodeAField.getText();
        String nodeB = nodeBField.getText();
        Main.getNodeMap().deleteEdge(nodeA, nodeB);
        redraw();
    }

    @FXML
    void pathingHoverStart(MouseEvent event) {
        String hoveredID = ((JFXButton) event.getSource()).getId();
        opacHandler(1, hoveredID);
    }

    @FXML
    void pathingHoverStop(MouseEvent event) {
        String hoveredID = ((JFXButton) event.getSource()).getId();
        opacHandler(.5, hoveredID);
    }

    void opacHandler(double opacity, String hoveredID) {
        try {
            switch (hoveredID) {
                case "astarBtn":
                    astarBtn.setOpacity(opacity);
                    break;
                case "depthBtn":
                    depthBtn.setOpacity(opacity);
                    break;
                case "breadthBtn":
                    breadthBtn.setOpacity(opacity);
                    break;
                case "dijkstrasBtn":
                    dijkstrasBtn.setOpacity(opacity);
                    break;
                case "beamBtn":
                    beamBtn.setOpacity(opacity);
                    break;
                case "bestBtn":
                    bestBtn.setOpacity(opacity);
                    break;
                case "editEmployeesBtn":
                    editEmployeesBtn.setOpacity(opacity);
                    break;
                case "serviceRequestBtn":
                    serviceRequestBtn.setOpacity(opacity);
                    break;
                case "addEditBtn":
                    addEditBtn.setOpacity(opacity);
                    break;
                case "removeNodeBtn":
                    removeNodeBtn.setOpacity(opacity);
                    break;
                case "RemoveButton":
                    RemoveButton.setOpacity(opacity);
                    break;
                case "Tup":
                    Tup.setOpacity(opacity);
                    break;
                case "Tdown":
                    Tdown.setOpacity(opacity);
                    break;
                case "Tleft":
                    Tleft.setOpacity(opacity);
                    break;
                case "Tright":
                    Tright.setOpacity(opacity);
                    break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void selectPathAlg(Event event) {
        String clickedID = ((JFXButton) event.getSource()).getId();
        resetPathBtns();
        switch (clickedID) {
            case "astarBtn":
                astarBtn.setStyle("-fx-background-color: #4286f4");
                this.single.getAlgorithm().setPathAlg(new astar());
                break;
            case "depthBtn":
                depthBtn.setStyle("-fx-background-color:  #4286f4");
                this.single.getAlgorithm().setPathAlg(new DepthFirst());
                break;
            case "breadthBtn":
                breadthBtn.setStyle("-fx-background-color:  #4286f4");
                this.single.getAlgorithm().setPathAlg(new BreadthFirst());
                break;
            case "dijkstrasBtn":
                dijkstrasBtn.setStyle("-fx-background-color:  #4286f4");
                this.single.getAlgorithm().setPathAlg(new Dijkstras());
                break;
            case "beamBtn":
                beamBtn.setStyle("-fx-background-color:  #4286f4");
                this.single.getAlgorithm().setPathAlg(new BeamFirst());
                break;
            case "bestBtn":
                bestBtn.setStyle("-fx-background-color:  #4286f4");
                //replaced for test perpouse return the class to best first!
                this.single.getAlgorithm().setPathAlg(new ScenicRoute());
                break;
                /* Scenic route button case
            case "scenicBtn":
                bestBtn.setStyle("-fx-background-color:  #4286f4");
                this.single.getAlgorithm().setPathAlg(new ScenicRoute());
                break;
                */
        }
    }

    void resetPathBtns() {
        astarBtn.setStyle("-fx-background-color: #21a047");
        depthBtn.setStyle("-fx-background-color:  #21a047");
        breadthBtn.setStyle("-fx-background-color:  #21a047");
        dijkstrasBtn.setStyle("-fx-background-color:  #21a047");
        beamBtn.setStyle("-fx-background-color:  #21a047");
        bestBtn.setStyle("-fx-background-color:  #21a047");
    }

    @FXML
    void adminLogout() { // Implement setter for pathanimation speed
        //setPathAnimationSpeed(pathSpeed.getValue());
        Main.getCurrStage().setScene(Main.getPatientScene());
    }

    @FXML
    void makeServiceRequest() throws IOException {
        FXMLLoader servContLoad = new FXMLLoader(getClass().getClassLoader().getResource("view/ui/ServiceRequest.fxml"));
        Parent root = servContLoad.load();
        ServiceController servCont = servContLoad.getController();
        Stage servStage = new Stage();
        servStage.setTitle("Service Request");
        servStage.setScene(new Scene(root, 243, 446));
        servCont.servLocField.setText(nodeIDField.getText());
        servStage.show();
    }

    @FXML
    void EditEmployees() throws IOException {
        FXMLLoader servContLoad = new FXMLLoader(getClass().getClassLoader().getResource("view/ui/ServiceEdit.fxml"));
        Parent root = servContLoad.load();
        ServiceEditController servCont = servContLoad.getController();
        Stage servStage = new Stage();
        servStage.setTitle("Service Request");
        servStage.setScene(new Scene(root, single.getMapWidth() - 100, single.getMapHeight() - 50));
        servStage.show();
    }

    @FXML
    void MyRequests() {
        CurrRequ.getItems().clear();
        Refresh();
        System.out.println("IBEHERE");
        Employee currEmp = Main.getCurrUser();
        System.out.println(Main.getRequestList().size());
        for (ServiceRequest aserv : Main.getRequestList()) {
            try {
                if (currEmp.getId() == aserv.getAssigned().getId()) {
                    CurrRequ.getItems().add(aserv.getAssigned().getId() + " | " + aserv.getSent().toString());
                }
            } catch (NullPointerException e) {
                e.getMessage();
            }
        }
    }

    @FXML
    void RemoveRequest() {
        try {
            ServiceRequest serv = null;
            String reqDate = CurrRequ.getValue().substring(CurrRequ.getValue().indexOf('|') + 2).trim();
            for (ServiceRequest aserv : Main.getRequestList()) {
                try {
                    System.out.println(aserv.getSent().toString());
                    System.out.println(reqDate);
                    if (aserv.getSent().toString().trim().equals(reqDate)) {

                        aserv.setActive(false);
                        serv = aserv;
                    }
                } catch (NullPointerException e) {
                    e.getMessage();
                }
            }

            if (serv != null) {
                Main.requests.remove(serv);
                serv.setReceived();
                serv.generateReport();
            }
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    void Refresh() {
        // Printing this stuff is a later order concern so get back to it later
        String finalString = " ";
        ArrayList<ServiceRequest> searchInactive = new ArrayList<ServiceRequest>();
        searchInactive.addAll(Main.requests);

        for (ServiceRequest serve : searchInactive) {
            if (serve.getAssigned() != null) {
                serve.resolveRequest();
                if (serve.isActive()) {
                    serve.generateReport();
                }
            } else {
                finalString.concat("No active Requests");
            }
        }
        for (ServiceRequest s : searchInactive) {
            if (!s.isActive()) {
                Main.requests.remove(s);
                try {
                    DeleteDB.delRequest(s.getSent().toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    void Zin() {
        System.out.println(zoomBar.getValue());
        single.setZoom(zoomBar.getValue());
        resize();

    }

    @FXML
    void Tleft() {
        single.addX((int) (200.0 / single.getZoom()));
        resize();
    }

    @FXML
    void Tright() {
        single.subX((int) (200.0 / single.getZoom()));
        resize();
    }

    @FXML
    void Tup() {
        single.addY((int) (160.0 / single.getZoom()));
        resize();
    }

    @FXML
    void Tdown() {
        single.subY((int) (160.0 / single.getZoom()));
        resize();
    }

    public void resize() {
        if (single.getZoom() <= 1) {
            single.setXTrans(0);
            single.setYTrans(0);
        }
        gc.setScaleX(single.getZoom());
        gc.setScaleY(single.getZoom());
        gc.setTranslateX(single.getXTrans());
        gc.setTranslateY(single.getYTrans());
        currentMap.setScaleX(single.getZoom());
        currentMap.setScaleY(single.getZoom());
        currentMap.setTranslateX(single.getXTrans());
        currentMap.setTranslateY(single.getYTrans());
        single.setMapWidth(currentMap.getFitWidth());
        single.setMapHeight(currentMap.getFitHeight());
    }

    int timeoutCounter = 0; // the number of seconds since the last mouse or key press
    int timeoutLimit = 15; // the number in seconds of no activity before automatic logout
    // Called on any mouse or key press in the admin pane.
    // Resets the counter of seconds since the last interaction.
    // Used for the automatic admin logout feature
    @FXML
    void resetTimeoutCounter() {
        System.out.println("whoa! a click event!");
        timeoutCounter = 0;
    }

    /*
     * autoLogoutHelper is called every second by the Timeline object
     * It is used to implement the automatic logout function from the admin screen.
     *
     * Every time it is run, it checks whether the timerCounter variable has reached timeoutLimit
     */
    private void autoLogoutHelper() {
        System.out.println("timeoutCounter = " + timeoutCounter);
        timeoutCounter++;

        if(timeoutCounter > timeoutLimit) {
            resetTimeoutCounter();
            Main.getCurrStage().setScene(Main.getPatientScene());
        }

        startTimer();
    }

    // this function and runLater create a timer to be used for admin auto-logout
    void startTimer() { // throws Exception {
        // only start a timer if we're in the admin scene. we don't care about the patient scene.
        runLater(javafx.util.Duration.seconds(1), () -> {
                    autoLogoutHelper();
                });
    }

    // helper for startTimer, which is used for admin auto-logout
    private void runLater(javafx.util.Duration delay, Runnable action) {
        Timeline timeline = new Timeline(new KeyFrame(delay, ae -> action.run()));
        timeline.play();
    }

    public void changePathSpeed(MouseEvent mouseEvent) {
        single.setPathAnimationSpeed((int)pathSpeed.getValue());
    }

}
