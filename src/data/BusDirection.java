/*
 *  Program: Symulacja przejazdu przez wąski most
 *     Plik: BusDirection.java
 *
 *  Enum BusDirection określa kierunek poruszania się busa.
 *
 *    Autor: Maciej Demucha
 *    Data:  14 stycznia 2022 r.
 */


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