<?php
// cek parameter jika ada dengan parameter "IDMhs";
if (isTheseParametersAvailable(array('IDInv', 'Nama', 'Jumlah', 'Kategori', 'Tipe', 'HargaBeli', 'TahunBeli', 'Foto1', 'Foto2'))) {
    $IDInv = $_POST['IDInv'];
    $Nama = $_POST['Nama'];
    $Jumlah = $_POST['Jumlah'];
    $Kategori = $_POST['Kategori'];
    $Tipe = $_POST['Tipe'];
    $HargaBeli = $_POST['HargaBeli'];
    $TahunBeli = $_POST['TahunBeli'];
    $Foto1 = $_POST['Foto1'];
    $Foto2 = $_POST['Foto2'];

    // membuat Query untuk menampilkan data sesuai dengan Kode
    $stmt = $conn->prepare("SELECT Kode, Nama, Jumlah, Kategori, Tipe, HargaBeli, TahunBeli, Foto FROM inventaris WHERE Kode=? ");

    $stmt->bind_param("s", $IDInv);
    $stmt->execute();
    $stmt->store_result();

    // jika data ada
    if ($stmt->num_rows > 0) {
        try {
            // menghapus foto
            $path = "Foto/" . $Foto1;
            unlink($path);

            // query delete data maintenance
            $stmt = $conn->prepare("DELETE FROM maintenance WHERE Kode=?");
            $stmt->bind_param("s", $IDInv);
            $stmt->execute();
            $stmt->close();

            // query delete data inventaris
            $stmt = $conn->prepare("DELETE FROM inventaris WHERE Kode=?");
            $stmt->bind_param("s", $IDInv);
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
        $response['message'] = 'Data dengan Kode ' . $IDInv . ' Tidak Ada ';
    }
}
?>