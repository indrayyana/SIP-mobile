<?php
if (isTheseParametersAvailable(array('IDInv', 'Nama', 'Jumlah', 'Kategori', 'Tipe', 'HargaBeli', 'TahunBeli', 'Foto1', 'Foto2'))) {
    // mendapatkan nilai dari params 
    $IDInv = $_POST['IDInv'];
    $Nama = $_POST['Nama'];
    $Jumlah = $_POST['Jumlah'];
    $Kategori = $_POST['Kategori'];
    $Tipe = $_POST['Tipe'];
    $HargaBeli = $_POST['HargaBeli'];
    $TahunBeli = $_POST['TahunBeli'];
    $Foto1 = $_POST['Foto1'];
    $Foto2 = $_POST['Foto2'];

    // membuat Query untuk menampilkan data sesuai dengan kode
    $stmt = $conn->prepare("SELECT Kode, Nama, Jumlah, Kategori, Tipe, HargaBeli, TahunBeli, Foto FROM inventaris WHERE Kode=? ");

    $stmt->bind_param("s", $IDInv);
    $stmt->execute();
    $stmt->store_result();

    // cek apakah sudah ada data dengan Kode tersebut
    if ($stmt->num_rows > 0) {
        $response['error'] = true;
        $response['message'] = 'Data dengan Kode ' . $IDInv . ' Sudah Ada ';
    } else {
        try {
            $stmt = $conn->prepare("INSERT INTO inventaris (Kode,Nama,Jumlah,Kategori,Tipe,HargaBeli,TahunBeli,Foto) VALUES (?,?,?,?,?,?,?,?)");

            $stmt->bind_param("ssissiis", $IDInv, $Nama, $Jumlah, $Kategori, $Tipe, $HargaBeli, $TahunBeli, $Foto2);
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