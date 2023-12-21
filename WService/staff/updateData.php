<?php
// cek parameter jika ada parameter "IDInv"
if (isTheseParametersAvailable(array('IDStaff', 'Nama', 'Jabatan', 'Tipe', 'Gaji', 'TahunBergabung'))) {
    // mendapatkan nilai dari params 
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

    // cek apakah sudah ada data dengan Kode tersebut
    if ($stmt->num_rows > 0) {
        try {
            // query untuk update data
            $stmt = $conn->prepare("UPDATE staff SET Nama=?,Jabatan=?,Tipe=?,Gaji=?,TahunBergabung=? WHERE Id=? ");

            $stmt->bind_param("sssiii", $Nama, $Jabatan, $Tipe, $Gaji, $TahunBergabung, $IDStaff);
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