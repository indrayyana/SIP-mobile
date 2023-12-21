<?php
// cek parameter jika ada parameter "IDStaff"
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
        $response['error'] = true;
        $response['message'] = 'Data dengan ID ' . $IDStaff . ' Sudah Ada ';
    } else {
        try {
            $stmt = $conn->prepare("INSERT INTO staff (Id,Nama,Jabatan,Tipe,Gaji,TahunBergabung) 
            VALUES (?,?,?,?,?,?)");

            $stmt->bind_param("isssii", $IDStaff, $Nama, $Jabatan, $Tipe, $Gaji, $TahunBergabung);
            $stmt->execute();
            $stmt->close();

            $response['error'] = false;
            $response['message'] = 'success';
        } catch (Exception $e) {
            $response['error'] = true;
            $response['message'] = 'Gagal';
        }
    }

}
?>