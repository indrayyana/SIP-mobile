<?php
if (isTheseParametersAvailable(array('IDMt', 'TanggalMaintenance', 'VendorMaintenance', 'StaffPIC'))) {
    // mendapatkan nilai dari params 
    $IDMt = $_POST['IDMt'];
    $TanggalMaintenance = $_POST['TanggalMaintenance'];
    $VendorMaintenance = $_POST['VendorMaintenance'];
    $StaffPIC = $_POST['StaffPIC'];

    // membuat Query untuk menampilkan data sesuai dengan Kode
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
    $stmt->execute();
    $stmt->store_result();


    // cek apakah sudah ada data inventaris dengan Kode tersebut
    if ($stmt->num_rows > 0) {
        // jika ada maka masukkan data maintenance dengan Kode yg sama
        try {
            $stmt = $conn->prepare("INSERT INTO maintenance (Kode,TanggalMaintenance,VendorMaintenance,StaffPIC) 
            VALUES (?,?,?,?)");

            $stmt->bind_param("ssss", $IDMt, $TanggalMaintenance, $VendorMaintenance, $StaffPIC);
            $stmt->execute();
            $stmt->close();

            $response['error'] = false;
            $response['message'] = 'success';
        } catch (Exception $e) {
            $response['error'] = true;
            $response['message'] = 'Data dengan ID ' . $IDMt . ' Sudah Ada';
        }
    } else {
        $response['error'] = true;
        $response['message'] = 'Data dengan ID ' . $IDMt . ' Tidak Ada ';
    }

}
?>