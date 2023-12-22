package com.example.sipmobile;

public class URLs {
    // URL untuk mengakses webservice
    public static final String SERVER_IP = "https://gdindra.my.id";
    public static final String ROOT_URL = SERVER_IP + "/WService/api.php?apicall=";

    // untuk memanggil web service CRUD Inventaris
    public static final String URL_INSERT_DATA = ROOT_URL + "insertDataInventory";
    public static final String URL_LOAD_DATA = ROOT_URL + "loadDataInventory";
    public static final String URL_UPDATE_DATA = ROOT_URL + "updateDataInventory";
    public static final String URL_DELETE_DATA = ROOT_URL + "deleteDataInventory";


    public static final String URL_LOADIMAGE = SERVER_IP + "/WService/Foto/";
    public static final String URL_UPLOADIMAGE = "WService/upload_image.php";
}
