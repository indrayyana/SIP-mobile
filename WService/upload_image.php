<?php
// Ini directory di server tempat foto disimpan
$target_dir = "Foto/";
$target_file_name = $target_dir . basename($_FILES["file"]["name"]);
$response = array();

// Pengecekan untuk mengetahui apakah target_dir merupakan directory atau bukan
if (!is_dir($target_dir)) {
    // jika bukan directory maka buat directory dengan full permissions
    mkdir($target_dir, 0755, true);
}

// Pengecekan untuk mengetahui bahwa yang diinputkan adalah file image
if (isset($_FILES["file"])) {
    // Proses upload jika berhasil menghasilkan nilai true
    if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file_name)) {
        $success = true;
        $message = "Successfully Uploaded";
    } else {
        $success = false;
        $message = "Error while uploading";
    }
} else {
    // Jika file bermasalah
    $success = false;
    $message = "Required File Missing";
}

// Hasil response
$response["success"] = $success;
$response["message"] = $message;
echo json_encode($response);
?>