package model;

public class Citation {

    private int paperId;
    private int citedPaperId;

    public Citation() {}

    public Citation(int paperId, int citedPaperId) {
        this.paperId = paperId;
        this.citedPaperId = citedPaperId;
    }

    public int getPaperId() { return paperId; }
    public void setPaperId(int paperId) { this.paperId = paperId; }

    public int getCitedPaperId() { return citedPaperId; }
    public void setCitedPaperId(int citedPaperId) { this.citedPaperId = citedPaperId; }

    @Override
    public String toString() {
        return paperId + " -> " + citedPaperId;
    }
}