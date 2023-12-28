<?php
if (isTheseParametersAvailable(array('IDStaff', 'Nama', 'Jabatan', 'Tipe', 'Gaji', 'TahunBergabung'))) {
    $IDStaff = $_POST['IDStaff'];
    $Nama = $_POST['Nama'];
    $Jabatan = $_POST['Jabatan'];
    $Tipe = $_POST['Tipe'];
    $Gaji = $_POST['Gaji'];
    $TahunBergabung = $_POST['TahunBergabung'];

    // membuat Query untuk menampilkan data sesuai dengan Id
    $stmt = $conn->prepare("SELECT Id, Nama, Jabatan, Tipe, Gaji, TahunBergabung FROM staff WHERE Id=? ");

    $stmt->bind_param("s", $IDStaff);
    $stmt->execute();
    $stmt->store_result();

    // jika data ada
    if ($stmt->num_rows > 0) {
        try {
            // query delete
            $stmt = $conn->prepare("DELETE FROM staff WHERE Id=?");
            $stmt->bind_param("s", $IDStaff);
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
        $response['message'] = 'Data dengan ID ' . $IDStaff . ' Tidak Ada ';
    }
}
?>