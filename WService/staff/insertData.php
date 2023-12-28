<?php
if (isTheseParametersAvailable(array('Nama', 'Jabatan', 'Tipe', 'Gaji', 'TahunBergabung'))) {
    // mendapatkan nilai dari params 
    $Nama = $_POST['Nama'];
    $Jabatan = $_POST['Jabatan'];
    $Tipe = $_POST['Tipe'];
    $Gaji = $_POST['Gaji'];
    $TahunBergabung = $_POST['TahunBergabung'];

    try {
        $stmt = $conn->prepare("INSERT INTO staff (Nama,Jabatan,Tipe,Gaji,TahunBergabung) 
        VALUES (?,?,?,?,?)");

        $stmt->bind_param("sssii", $Nama, $Jabatan, $Tipe, $Gaji, $TahunBergabung);
        $stmt->execute();
        $stmt->close();

        $response['error'] = false;
        $response['message'] = 'success';
    } catch (Exception $e) {
        $response['error'] = true;
        $response['message'] = 'Gagal';
    }
}
?>