package com.example.sipmobile;

public class URLs {
    // URL untuk mengakses webservice
    public static final String SERVER_IP = "https://gdindra.my.id";
    public static final String ROOT_URL = SERVER_IP + "/WService/api.php?apicall=";

    // untuk memanggil web service CRUD Inventaris
    public static final String URL_INSERT_DATA_INVENTARIS = ROOT_URL + "insertDataInventory";
    public static final String URL_LOAD_DATA_INVENTARIS = ROOT_URL + "loadDataInventory";
    public static final String URL_UPDATE_DATA_INVENTARIS = ROOT_URL + "updateDataInventory";
    public static final String URL_DELETE_DATA_INVENTARIS = ROOT_URL + "deleteDataInventory";

    // untuk memanggil web service CRUD Staff
    public static final String URL_INSERT_DATA_STAFF = ROOT_URL + "insertDataStaff";
    public static final String URL_LOAD_DATA_STAFF = ROOT_URL + "loadDataStaff";
    public static final String URL_UPDATE_DATA_STAFF = ROOT_URL + "updateDataStaff";
    public static final String URL_DELETE_DATA_STAFF = ROOT_URL + "deleteDataStaff";

    public static final String URL_LOADIMAGE = SERVER_IP + "/WService/Foto/";
    public static final String URL_UPLOADIMAGE = "WService/upload_image.php";
}
