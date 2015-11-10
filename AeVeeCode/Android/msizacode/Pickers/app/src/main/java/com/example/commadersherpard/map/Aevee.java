package com.example.commadersherpard.map;

import java.util.ArrayList;

/**
 * Created by Vukosi on 2015-10-24.
 */
public class Aevee
{
    static public user Current=null;
    static public int statusBit=0;
    static public int count=0;
    static public double curPrice=0;
    static public String [] ProductList={"LCD HD SAMSUNG TV","XBOX","Braai Vorse","5 Piece Office Set","4 cusins wine"};
    static public double [] ProductList1Price={10000.00,6999.00,39.99,12000.00,149.00};
    static public String [] ProductList2={"Shirt","Pants","Shoes","Socks","Dress"};
    static public double [] ProductList2Price={150.00,45.00,300.99,50.99,300.00};
    static public String [] ShopList={"GAME","Jet"};
    static public ArrayList<String> currentlist= new ArrayList<String>();
    static public Boolean swi=false;

    static public Double priceCheck(String s)
    {
        double price=0;
        for(int i=0;i<ProductList1Price.length;i++)
        {
            if(s.equals(ProductList[i]))
                return ProductList1Price[i];
            else if(s.equals(ProductList2[i]))
                return ProductList2Price[i];
        }
        return 0.00;
    }
}
