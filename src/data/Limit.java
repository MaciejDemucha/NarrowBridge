package data;

public enum Limit {
    NO_LIMIT("Bez ograniczeń"),
    THREE_BUS("Ruch dwukierunkowy (max 3 busy)"),
    THREE_BUS_ONE_DIRECTION("Ruch jednokierunkowy (max 3 busy)"),
    ONE_BUS("Ruch ograniczony (max 1 bus)");

    String name;

    Limit(String name){
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }

}
