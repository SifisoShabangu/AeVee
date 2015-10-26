package com.example.commadersherpard.map;

/**
 * Created by Vukosi on 2015-10-24.
 */
public class user {

    String usrname;
    String usrpassword;
    String surname;
    String email;
    Avee_Address fullAdress;


    public class Avee_Address
    {
         String streetName;
         String streetNumber;
         String Province;
         String code;
         String city;


        public Avee_Address()
        {
            streetName="";
            streetNumber="";
            city="";
            Province="";
            code="";
        }
        public Avee_Address(String ...vals)
        {
            this.setStreetName(vals[0]);
            this.setProvince(vals[3]);
            this.setCity(vals[2]);
            this.setStreetNumber(vals[1]);
            this.setCode(vals[4]);
        }

        @Override
        public String toString()
        {
            return getStreetNumber()+" "+getStreetName()+" "+getCity()+" "+getProvince()+" "+getCode();
        }
        public String getCode() {
            return code;
        }

        public String getCity() {
            return city;
        }

        public String getProvince() {
            return Province;
        }

        public String getStreetName() {
            return streetName;
        }

        public String getStreetNumber() {
            return streetNumber;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setProvince(String province) {
            Province = province;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public void setStreetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
        }
    }
}
