package model;

public class PathingContainer {
    public static PathingAlgorithm pathAlg;

    public PathingContainer() {
    }

    public PathingAlgorithm getPathAlg() {
        return pathAlg;
    }

    public void setPathAlg(PathingAlgorithm pathAlg) {
        this.pathAlg = pathAlg;
    }
}
