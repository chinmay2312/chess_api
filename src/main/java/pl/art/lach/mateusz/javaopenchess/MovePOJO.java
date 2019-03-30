package pl.art.lach.mateusz.javaopenchess;

public class MovePOJO {
    private String startSq;
    private String endSq;
    private String movedPiece;
    private String takenPiece;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartSq() {
        return startSq;
    }

    public void setStartSq(String startSq) {
        this.startSq = startSq;
    }

    public String getEndSq() {
        return endSq;
    }

    public void setEndSq(String endSq) {
        this.endSq = endSq;
    }

    public String getMovedPiece() {
        return movedPiece;
    }

    public void setMovedPiece(String movedPiece) {
        this.movedPiece = movedPiece;
    }

    public String getTakenPiece() {
        return takenPiece;
    }

    public void setTakenPiece(String takenPiece) {
        this.takenPiece = takenPiece;
    }

}
