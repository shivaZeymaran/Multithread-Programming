/**
 * This class represents a car that has two attributes
 */
class Car {
    int start;
    int end;

    /**
     * The only constructor of the class
     * @param start number of street that this car wants to start it's movement from
     * @param end number of street that it wants to go to
     */
    Car(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /**
     * This method should be called to add the current car to the proper street list
     */
    void add_to_list(){
        if (this.start == 1)
            Street.west_list.add(this);
        else if(this.start == 2)
            Street.south_list.add(this);
        else if(this.start == 3)
            Street.east_list.add(this);
        else
            Street.north_list.add(this);
    }

    /**
     * This method used to compare two cars with their attributes
     * @param obj the object is a car
     */
    @Override
    public boolean equals(Object obj) {
        return this.start == ((Car)obj).start && this.end == ((Car)obj).end;
    }
}

