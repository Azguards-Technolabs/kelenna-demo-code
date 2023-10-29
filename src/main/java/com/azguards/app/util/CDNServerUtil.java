package com.azguards.app.util;

import java.net.URLEncoder;

public class CDNServerUtil {
	public static String baseUrl = "https://1776366103.rsc.cdn77.org/SEEKA_IMG/IMGES";
	public static Integer cityImageStartIndex = 51;
	public static Integer countryImageStartIndex = 1;
	public static Integer instituteImageStartIndex = 1;
	
	public static String getInstituteLogoImage(String countryName, String instituteName) {
		try {
			return baseUrl+"/"+URLEncoder.encode(countryName, "UTF-8").replace("+", "%20")+"/Logos/"+URLEncoder.encode(instituteName, "UTF-8").replace("+", "%20")+".jpg";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getInstituteMainImage(String countryName, String instituteName) {
		try {
			return baseUrl+"/"+URLEncoder.encode(countryName, "UTF-8").replace("+", "%20")+"/Universities/"+URLEncoder.encode(instituteName, "UTF-8").replace("+", "%20")+"/"+"1.jpg";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getInstituteImages(String countryName, String instituteName,int i) {
        try {
            return baseUrl+"/"+URLEncoder.encode(countryName, "UTF-8").replace("+", "%20")+"/Universities/"+URLEncoder.encode(instituteName, "UTF-8").replace("+", "%20")+"/"+i+".jpg";
        }catch(Exception e) {
            e.printStackTrace();
        }
        return "";
    }
	
	
	public static String getCityImage(String countryName, String cityName,Integer index) {
		try {
			return baseUrl+"/"+URLEncoder.encode(countryName, "UTF-8").replace("+", "%20")+"/Cities/"+URLEncoder.encode(cityName, "UTF-8").replace("+", "%20")+"/"+index+".jpg";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getCountryImageUrl(String countryName) {
		try {
			return baseUrl+"/"+URLEncoder.encode(countryName, "UTF-8").replace("+", "%20")+"/"+URLEncoder.encode(countryName, "UTF-8").replace("+", "%20")+".jpg";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getServiceIconUrl(String services) {
		try {
			return baseUrl+"/Services/"+URLEncoder.encode(services.replaceAll("/", " "), "UTF-8").replace("+", "%20")+".png";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getFacultyIconUrl(String facultyName) {
		try { 
			return baseUrl+"/Faculty/"+URLEncoder.encode(facultyName, "UTF-8").replace("+", "%20")+".png";
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static void main(String[] args) {
        System.out.println(getServiceIconUrl("Visa Work Benefits"));
    }
}
