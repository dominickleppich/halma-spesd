package halmaPP.preset;

public class Status implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int OK       = 0;
    public static final int REDWIN   = 1;
    public static final int BLUEWIN  = 2;
    public static final int DRAW     = 3;
    public static final int ILLEGAL  = 9;
    public static final int ERROR    = 99;

    public Status(int value) { 
        this.value = value; 
    }

    //----------------------------------------------------------------
    public boolean isOK() { 
        return value == OK;
    }

    public boolean isREDWIN() {
        return value == REDWIN;
    }

    public boolean isBLUEWIN() {
        return value == BLUEWIN;
    }

    public boolean isDRAW() {
        return value == DRAW;
    }

    public boolean isILLEGAL() {
        return value == ILLEGAL;
    }

    public boolean isERROR() {
        return value == ERROR;
    }

    //----------------------------------------------------------------
    public int getValue () {
       return value ;
    }

    public void setValue (int value) {
        this.value = value ;
    }

    //----------------------------------------------------------------
    public String toString() {
	String s = "";

	switch (value) { 
	case OK: 
	    s = "ok";
	    break;
	case REDWIN:
	    s = "red wins";
	    break;
	case BLUEWIN:
	    s = "blue wins";
	    break;
	case DRAW: 
	    s = "draw";
	    break;
	case ILLEGAL: 
	    s = "illegal";
	    break;
	case ERROR:
	default:
	    s = "error";
	}

	return s;
    }


    // private -------------------------------------------------------
    private int value = OK;
}
