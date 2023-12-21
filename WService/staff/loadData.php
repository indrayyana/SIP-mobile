<?php

if (isTheseParametersAvailable(array('IDStaff'))) {
    $IDStaff = $_POST['IDStaff'];

    if (strcmp($IDStaff, "Kosong") == 0) {
        $stmt = $conn->prepare("SELECT Id, Nama, Jabatan, Tipe, Gaji, TahunBergabung FROM staff");
    } else {
        $stmt = $conn->prepare("SELECT Id, Nama, Jabatan, Tipe, Gaji, TahunBergabung FROM staff WHERE Kode=? ");
        $stmt->bind_param("s", $IDStaff);
    }

    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($Id, $Nama, $Jabatan, $Tipe, $Gaji, $TahunBergabung);
        $dataStaff = array();

        while ($stmt->fetch()) {
            $tempData = array();

            $tempData['Id'] = $Id;
            $tempData['Nama'] = $Nama;
            $tempData['Jabatan'] = $Jabatan;
            $tempData['Tipe'] = $Tipe;
            $tempData['Gaji'] = $Gaji;
            $tempData['TahunBergabung'] = $TahunBergabung;

            array_push($dataStaff, $tempData);
        }

        $response['error'] = false;
        $response['message'] = "Success";
        $response['data'] = $dataStaff;
    } else {
        $response['error'] = true;
        if (strcmp($IDStaff, "Kosong") == 0) {
            $response['message'] = "Data Tidak Ada";
        } else {
            $response['message'] = "Data Tidak Ada dengan ID: " . $IDStaff;
        }
    }
}

?>