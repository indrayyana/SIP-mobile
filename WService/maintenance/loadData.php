<?php

if (isTheseParametersAvailable(array('Nama'))) {
    $Nama = $_POST['Nama'];

    if (strcmp($Nama, "Kosong") == 0) {
        $stmt = $conn->prepare("SELECT 
                                A.Kode, 
                                A.Nama, 
                                A.Foto,
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
                                A.Foto,
                                B.TanggalMaintenance, 
                                B.VendorMaintenance, 
                                B.StaffPIC 
                            FROM inventaris AS A
                            LEFT JOIN maintenance AS B 
                            ON A.Kode = B.Kode
                            WHERE A.Nama LIKE ? ");
        $wildcard = "%$Nama%";
        $stmt->bind_param("s", $wildcard);
    }

    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($Kode, $Nama, $Foto, $TanggalMaintenance, $VendorMaintenance, $StaffPIC);
        $dataMaintenance = array();

        while ($stmt->fetch()) {
            $tempData = array();

            $tempData['Kode'] = $Kode;
            $tempData['Nama'] = $Nama;
            $tempData['Foto'] = $Foto;
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
        if (strcmp($Nama, "Kosong") == 0) {
            $response['message'] = "Data Tidak Ada";
        } else {
            $response['message'] = "Data Tidak Ada dengan Kode: " . $IDMt;
        }
    }
}

?>