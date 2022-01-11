package data;

public enum BusDirection{
    EAST,
    WEST;

    @Override
    public String toString(){
        switch(this){
            case EAST: return "W";
            case WEST: return "Z";
        }
        return "";
    }
}