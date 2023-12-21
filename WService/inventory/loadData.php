<?php

if (isTheseParametersAvailable(array('IDInv'))) {
    $IDInv = $_POST['IDInv'];

    if (strcmp($IDInv, "Kosong") == 0) {
        $stmt = $conn->prepare("SELECT Kode, Nama, Jumlah, Kategori, Tipe, HargaBeli, TahunBeli, Foto FROM inventaris");
    } else {
        $stmt = $conn->prepare("SELECT Kode, Nama, Jumlah, Kategori, Tipe, HargaBeli, TahunBeli, Foto FROM inventaris WHERE Kode=? ");

        //mendeklarasikan param di query "?" dengan tipe data string "s" yg nilainya adalah IDInv
        $stmt->bind_param("s", $IDInv);
    }

    $stmt->execute();
    $stmt->store_result();

    if ($stmt->num_rows > 0) {
        $stmt->bind_result($Kode, $Nama, $Jumlah, $Kategori, $Tipe, $HargaBeli, $TahunBeli, $Foto);

        $dataInventaris = array();

        while ($stmt->fetch()) {
            $tempData = array();

            $tempData['Kode'] = $Kode;
            $tempData['Nama'] = $Nama;
            $tempData['Jumlah'] = $Jumlah;
            $tempData['Kategori'] = $Kategori;
            $tempData['Tipe'] = $Tipe;
            $tempData['HargaBeli'] = $HargaBeli;
            $tempData['TahunBeli'] = $TahunBeli;
            $tempData['Foto'] = $Foto;

            array_push($dataInventaris, $tempData);
        }

        $response['error'] = false;
        $response['message'] = "Success";
        $response['data'] = $dataInventaris;
    } else {
        $response['error'] = true;
        if (strcmp($IDInv, "Kosong") == 0) {
            $response['message'] = "Data Tidak Ada";
        } else {
            $response['message'] = "Data Tidak Ada dengan Kode: " . $IDInv;
        }
    }
}

?>