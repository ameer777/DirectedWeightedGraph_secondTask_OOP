package api;

public class geoLocation implements geo_location {

    double x,y,z,dist;

    public geoLocation(){
        this.x=0;
        this.y=0;
        this.z=0;
    }
    public geoLocation(String pos){

        int j=0;
        int index=0;
        for (int i=0 ; i<pos.length() ; i++){
            if(i==pos.length()-1){
                String temp = pos.substring(index , i);
                this.z=Double.parseDouble(temp);
                break;
            }
            if(pos.charAt(i)==','){
                String temp = pos.substring(index , i);
                index=i+1;
                if(j==0) {this.x = Double.parseDouble(temp); j++;}
                else {this.y = Double.parseDouble(temp);}
            }
        }
    }


    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(geo_location g) {
        double x1= x-g.x();
        double y1= y-g.y();
        double z1= z-g.z();
        double dist1 = x1*x1+y1*y1+z1*z1;
        return Math.sqrt(dist1);
    }
}