<?php

if (isTheseParametersAvailable(array('IDMt'))) {
    $IDMt = $_POST['IDMt'];

    if (strcmp($IDMt, "Kosong") == 0) {
        $stmt = $conn->prepare("SELECT 
                                A.Kode, 
                                A.Nama, 
                                B.TanggalMaintenance, 
                                B.VendorMaintenance, 
                                B.StaffPIC 
                            FROM inventaris AS A
                            LEFT JOIN maintenance AS B
                            ON A.Kode = B.Kode");
    } else {
        $stmt = $conn->prepare("SELECT 
                                A.Kode,
                                A.Nama, 
                                B.TanggalMaintenance, 
                                B.VendorMaintenance, 
                                B.StaffPIC 
                            FROM inventaris AS A
                            LEFT JOIN maintenance AS B 
                            ON A.Kode = B.Kode
                            WHERE A.Kode=? ");
        $stmt->bind_param("s", $IDMt);
    }

    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($Kode, $Nama, $TanggalMaintenance, $VendorMaintenance, $StaffPIC);
        $dataMaintenance = array();

        while ($stmt->fetch()) {
            $tempData = array();

            $tempData['Kode'] = $Kode;
            $tempData['Nama'] = $Nama;
            $tempData['TanggalMaintenance'] = $TanggalMaintenance;
            $tempData['VendorMaintenance'] = $VendorMaintenance;
            $tempData['StaffPIC'] = $StaffPIC;

            array_push($dataMaintenance, $tempData);
        }

        $response['error'] = false;
        $response['message'] = "Success";
        $response['data'] = $dataMaintenance;
    } else {
        $response['error'] = true;
        if (strcmp($IDMt, "Kosong") == 0) {
            $response['message'] = "Data Tidak Ada";
        } else {
            $response['message'] = "Data Tidak Ada dengan Kode: " . $IDMt;
        }
    }
}

?>