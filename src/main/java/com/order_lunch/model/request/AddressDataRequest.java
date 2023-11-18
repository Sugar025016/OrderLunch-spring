package com.order_lunch.model.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDataRequest {

    private String cityName;

    private List<Area> area = new ArrayList<Area>();



    public AddressDataRequest() {
    }

    public AddressDataRequest(String cityName, List<Area> area) {
        this.cityName = cityName;
        this.area = area;
    }


    public String getCityName() {
        return cityName;
    }



    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



    public List<Area> getArea() {
        return area;
    }



    public void setArea(List<Area> area) {
        this.area = area;
    }

    public static class Area {

        private String areaName;
        private List<Street> streets= new ArrayList<Street>();



        public Area() {
        }
        

        public Area(String areaName, List<Street> streets) {
            this.areaName = areaName;
            this.streets = streets;
        }



        public String getAreaName() {
            return areaName;
        }




        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }


        public List<Street> getStreets() {
            return streets;
        }


        public void setStreets(List<Street> streets) {
            this.streets = streets;
        }



        public static class Street {

            private int streetKey;
            private String streetName;

                        
            public Street() {
            }
            public Street(int streetKey, String streetName) {
                this.streetKey = streetKey;
                this.streetName = streetName;
            }

            public int getStreetKey() {
                return streetKey;
            }
            public void setStreetKey(int streetKey) {
                this.streetKey = streetKey;
            }
            public String getStreetName() {
                return streetName;
            }
            public void setStreetName(String streetName) {
                this.streetName = streetName;
            }

            

        }






    }




}