<?php
if (isTheseParametersAvailable(array('IDMt', 'TanggalMaintenance', 'VendorMaintenance', 'StaffPIC'))) {
    // mendapatkan nilai dari params 
    $IDMt = $_POST['IDMt'];
    $TanggalMaintenance = $_POST['TanggalMaintenance'];
    $VendorMaintenance = $_POST['VendorMaintenance'];
    $StaffPIC = $_POST['StaffPIC'];

    // membuat Query untuk menampilkan data sesuai dengan Id
    $stmt = $conn->prepare("SELECT Kode, TanggalMaintenance, VendorMaintenance, StaffPIC FROM maintenance WHERE Kode=? ");

    $stmt->bind_param("s", $IDMt);
    $stmt->execute();
    $stmt->store_result();

    // cek apakah sudah ada data dengan Kode tersebut
    if ($stmt->num_rows > 0) {
        try {
            // query untuk update data
            $stmt = $conn->prepare("UPDATE maintenance SET TanggalMaintenance=?,VendorMaintenance=?,StaffPIC=? WHERE Kode=? ");

            $stmt->bind_param("ssss", $TanggalMaintenance, $VendorMaintenance, $StaffPIC, $IDMt);
            $stmt->execute();
            $stmt->close();

            $response['error'] = false;
            $response['message'] = 'success';
        } catch (Exception $e) {
            $response['error'] = true;
            $response['message'] = 'Gagal';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Data dengan ID ' . $IDMt . ' Tidak Ada ';
    }

}
?>